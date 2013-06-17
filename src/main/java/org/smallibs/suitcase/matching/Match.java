/*
 * Copyright (C)2013 D. Plaindoux.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; see the file COPYING.  If not, write to
 * the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.smallibs.suitcase.matching;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.pattern.core.CallBack;
import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.utils.Function0;
import org.smallibs.suitcase.utils.Option;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public final class Match<T, R> {

    // =================================================================================================================
    // Internal classes and intermediate code for DSL like approach
    // =================================================================================================================

    private class Rule {
        private final Class<?> type;
        private final Case<T> aCase;
        private final CallBack callBack;

        private Rule(Class<T> type, Case<T> aCase, CallBack callBack) {
            this.type = type;
            this.aCase = aCase;
            this.callBack = callBack;
        }

        protected boolean typeIsCorrect(Object object) {
            return type == null || !Cases.typeOf(type).unapply(object).isNone();
        }

        R apply(T object) throws MatchingException {
            if (this.typeIsCorrect(object)) {
                final Option<List<Object>> result = aCase.unapply(object);
                if (!result.isNone()) {
                    try {
                        final Object[] parameters = result.value().toArray(new Object[result.value().size()]);
                        for (Method method : callBack.getClass().getMethods()) {
                            if (method.getName().equals("apply") && method.getParameterTypes().length == parameters.length) {
                                return (R) method.invoke(callBack, parameters);
                            }
                        }

                        throw new NoSuchMethodException();
                    } catch (ClassCastException e) {
                        throw new IllegalStateException(e);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    } catch (InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }

            throw new MatchingException();
        }
    }

    // =================================================================================================================

    public class When {
        private final Class<T> type;
        private final Case<T> aCase;

        public When(Class<T> type, Case<T> aCase) {
            this.type = type;
            this.aCase = aCase;
        }

        public Match<T, R> then(final R result) {
            return then(new Function0<R>() {
                public R apply() {
                    return result;
                }
            });
        }

        public Match<T, R> then(CallBack function) {
            Match.this.rules.add(new Rule(type, aCase, function));
            return Match.this;
        }
    }

    // =================================================================================================================
    // Attributes
    // =================================================================================================================

    private final LinkedList<Rule> rules;

    // =================================================================================================================
    // Factory
    // =================================================================================================================

    public static <T, R> Match<T, R> match() {
        return new Match<>();
    }

    // =================================================================================================================
    // Constructors
    // =================================================================================================================

    public Match() {
        this.rules = new LinkedList<>();
    }

    // =================================================================================================================
    // Behaviors
    // =================================================================================================================

    public When when(final T object) {
        return when(Cases.<T>reify(object));
    }

    public When when(final Cases.AnyValueObject _) {
        return when(Cases.<T>any());
    }

    public When when(final Case<T> aCase) {
        if (aCase == null) {
            return new When(null, Cases.<T>nil());
        } else {
            return new When(this.<T>getType(aCase), aCase);
        }
    }

    private <T> Class<T> getType(Case aCase) {
        if (aCase.getClass().isAnnotationPresent(CaseType.class)) {
            final CaseType caseType = aCase.getClass().getAnnotation(CaseType.class);
            return (Class<T>) caseType.value();
        } else {
            return null;
        }
    }

    public R apply(T object) throws MatchingException {
        for (Rule rule : rules) {
            try {
                return rule.apply(object);
            } catch (MatchingException me) {
                // Skip
            }
        }

        throw new MatchingException();
    }
}