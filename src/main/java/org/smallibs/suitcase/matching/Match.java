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
import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.utils.Function;
import org.smallibs.suitcase.utils.Function0;
import org.smallibs.suitcase.utils.Function1;
import org.smallibs.suitcase.utils.Function2;
import org.smallibs.suitcase.utils.Function3;
import org.smallibs.suitcase.utils.FunctionN;
import org.smallibs.suitcase.utils.Option;

import java.util.LinkedList;
import java.util.List;

public final class Match<T, R> implements Function1<T, R> {

    // =================================================================================================================
    // Internal classes and intermediate code for DSL like approach
    // =================================================================================================================

    private class Rule implements Function1<List<Object>, R> {
        private final Class<?> type;
        private final Case<T> aCase;
        private final Function function;

        private Rule(Class<?> type, Case<T> aCase, Function function) {
            this.type = type;
            this.aCase = aCase;
            this.function = function;
        }

        protected boolean typeIsCorrect(Object object) {
            return type == null || !Cases.typeOf(type).unapply(object).isNone();
        }

        Option<List<Object>> match(T object) throws MatchingException {
            if (this.typeIsCorrect(object)) {
                return aCase.unapply(object);
            } else {
                return new Option.None<>();
            }
        }

        @SuppressWarnings("unchecked")
        public R apply(List<Object> result) throws MatchingException {
            try {
                final Object[] parameters = result.toArray(new Object[result.size()]);
                switch (parameters.length) {
                    case 0:
                        return ((Function0<R>) function).apply();
                    case 1:
                        return ((Function1<Object, R>) function).apply(parameters[0]);
                    case 2:
                        return ((Function2<Object, Object, R>) function).apply(parameters[0], parameters[1]);
                    case 3:
                        return ((Function3<Object, Object, Object, R>) function).apply(parameters[0], parameters[1], parameters[2]);
                    default:
                        return ((FunctionN<R>) function).apply(parameters);
                }
            } catch (ClassCastException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    // =================================================================================================================

    abstract class When {
        protected final Class<?> type;
        protected final Case<T> aCase;

        public When(Class<?> type, Case<T> aCase) {
            this.type = type;
            this.aCase = aCase;
        }

        abstract public Match<T, R> then(final R result);

        public Match<T, R> then(Function callBack) {
            Match.this.rules.add(new Rule(type, aCase, callBack));
            return Match.this;
        }
    }

    class When0 extends When {
        public When0(Class<?> type, Case<T> aCase) {
            super(type, aCase);
        }

        public Match<T, R> then(final R result) {
            return then(new Function0<R>() {
                public R apply() {
                    return result;
                }
            });
        }
    }

    class When1 extends When {
        public When1(Class<?> type, Case<T> aCase) {
            super(type, aCase);
        }

        public Match<T, R> then(final R result) {
            return then(new Function1<Object, R>() {
                public R apply(Object o1) {
                    return result;
                }
            });
        }
    }

    class When2 extends When {
        public When2(Class<?> type, Case<T> aCase) {
            super(type, aCase);
        }

        public Match<T, R> then(final R result) {
            return then(new Function2<Object, Object, R>() {
                public R apply(Object o1, Object o2) {
                    return result;
                }
            });
        }
    }

    class When3 extends When {
        public When3(Class<?> type, Case<T> aCase) {
            super(type, aCase);
        }

        public Match<T, R> then(final R result) {
            return then(new Function3<Object, Object, Object, R>() {
                public R apply(Object o1, Object o2, Object o3) {
                    return result;
                }
            });
        }
    }

    class WhenN extends When {
        public WhenN(Class<?> type, Case<T> aCase) {
            super(type, aCase);
        }

        public Match<T, R> then(final R result) {
            return then(new FunctionN<R>() {
                public R apply(Object... o1) {
                    return result;
                }
            });
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
        return when(Cases.<T>fromObject(object));
    }

    public When when(final Class<? extends T> object) {
        return when(Cases.<T>fromObject(object));
    }

    public When when(final Cases.AnyObject _) {
        return when(Cases.<T>any());
    }

    public When when(final Cases.VariableObject var) {
        return when(var.of(Cases.<T>any()));
    }

    public When when(final Case<T> aCase) {
        if (aCase == null) {
            return new When0(null, Cases.<T>nil());
        } else {
            final Class<?> type = this.getType(aCase);

            switch (aCase.numberOfVariables()) {
                case 0:
                    return new When0(type, aCase);
                case 1:
                    return new When1(type, aCase);
                case 2:
                    return new When2(type, aCase);
                case 3:
                    return new When3(type, aCase);
                default:
                    return new WhenN(type, aCase);
            }
        }
    }

    private Class<?> getType(Case aCase) {
        if (aCase.getClass().isAnnotationPresent(CaseType.class)) {
            return aCase.getClass().getAnnotation(CaseType.class).value();
        } else {
            return null;
        }
    }

    // =================================================================================================================

    public R apply(T object) throws MatchingException {
        for (Rule rule : rules) {
            final Option<List<Object>> option = rule.match(object);
            if (!option.isNone()) {
                return rule.apply(option.value());
            }
        }

        throw new MatchingException();
    }
}