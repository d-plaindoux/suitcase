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
import org.smallibs.suitcase.callback.CallBack;
import org.smallibs.suitcase.callback.CallBacks;
import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.pattern.core.Cases;
import org.smallibs.suitcase.utils.Option;

import java.util.LinkedList;

public final class Match<T, R> {

    // =================================================================================================================
    // Internal classes and intermediate code for DSL like approach
    // =================================================================================================================

    private class Rule<M> {
        private final Class<T> type;
        private final Case<T, M> aCase;
        private final CallBack<M, R> callBack;

        private Rule(Class<T> type, Case<T, M> aCase, CallBack<M, R> callBack) {
            this.type = type;
            this.aCase = aCase;
            this.callBack = callBack;
        }

        private Case<T, M> getTypedPattern() {
            return new Case<T, M>() {
                @Override
                public Option<M> unapply(T object) {
                    if (type != null && Cases.typeOf(type).unapply(object).isNone()) {
                        return new Option.None<M>();
                    } else {
                        return aCase.unapply(object);
                    }
                }
            };
        }

        private CallBack<M, R> getCallBack() {
            return callBack;
        }
    }

    public class With<M> {
        private final Class<T> type;
        private final Case<T, M> aCase;

        public With(Class<T> type, Case<T, M> aCase) {
            this.type = type;
            this.aCase = aCase;
        }

        public Match<T, R> then(R result) {
            return then(CallBacks.<M, R>constant(result));
        }

        public Match<T, R> then(CallBack<M, R> callBack) {
            Match.this.rules.add(new Rule<M>(type, aCase, callBack));
            return Match.this;
        }
    }


    // =================================================================================================================
    // Attributes
    // =================================================================================================================

    private final LinkedList<Rule<?>> rules;

    // =================================================================================================================
    // Factory
    // =================================================================================================================

    public static <T, R> Match<T, R> match() {
        return new Match<T, R>();
    }

    // =================================================================================================================
    // Constructors
    // =================================================================================================================

    public Match() {
        this.rules = new LinkedList<Rule<?>>();
    }

    // =================================================================================================================
    // Behaviors
    // =================================================================================================================

    public With<T> with(final T object) {
        if (object == null) {
            return with(Cases.<T>nil(), null);
        } else if (object instanceof Class<?>) {
            return withType((Class) object);
        } else {
            return with(Cases.<T>constant(object), null);
        }
    }

    private <M> With<M> withType(final Class<M> pojo) {
        return with(Cases.<T, M>typeOf(pojo), null);
    }

    public <M> With<M> with(final Case<T, M> aCase) {
        if (aCase == null) {
            return with(Cases.<T>nil(), null);
        } else if (aCase.getClass().isAnnotationPresent(CaseType.class)) {
            final CaseType caseType = aCase.getClass().getAnnotation(CaseType.class);
            return with(aCase, (Class<T>) caseType.value());
        } else {
            return with(aCase, null);
        }
    }

    public <M> With with(final Case<T, M> aCase, final Class<T> type) {
        return new With(type, aCase);
    }

    public <M> R apply(Rule<M> rule, T object) throws MatchingException {
        final Option<M> result = rule.getTypedPattern().unapply(object);
        if (result.isNone()) {
            throw new MatchingException();
        } else {
            return rule.getCallBack().apply(result.value());
        }

    }

    public R apply(T object) throws MatchingException {
        for (Rule<?> rule : rules) {
            try {
                return apply(rule, object);
            } catch (MatchingException me) {
                // Skip
            }
        }

        throw new MatchingException();
    }
}