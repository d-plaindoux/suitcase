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
import org.smallibs.suitcase.pattern.Case;
import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.pattern.prototype.Case0;
import org.smallibs.suitcase.pattern.prototype.Case1;
import org.smallibs.suitcase.pattern.prototype.Case2;
import org.smallibs.suitcase.pattern.prototype.Case3;
import org.smallibs.suitcase.utils.Function0;
import org.smallibs.suitcase.utils.Function1;
import org.smallibs.suitcase.utils.Function2;
import org.smallibs.suitcase.utils.Function3;
import org.smallibs.suitcase.utils.Functions;
import org.smallibs.suitcase.utils.Option;
import org.smallibs.suitcase.utils.Tuple2;
import org.smallibs.suitcase.utils.Tuple3;

import java.util.LinkedList;

public final class Match<T, R> {

    // =================================================================================================================
    // Internal classes and intermediate code for DSL like approach
    // =================================================================================================================

    abstract private class Rule {
        private final Class<?> type;

        private Rule(Class<?> type) {
            this.type = type;
        }

        protected boolean typeIsCorrect(Object object) {
            return type == null || !Cases.typeOf(type).unapply(object).isNone();
        }

        abstract R apply(T object) throws MatchingException;
    }

    private class Rule0 extends Rule {
        private final Case0<T> aCase;
        private final Function0<R> function;

        private Rule0(Class<T> type, Case0<T> aCase, Function0<R> function) {
            super(type);
            this.aCase = aCase;
            this.function = function;
        }

        R apply(T object) throws MatchingException {
            if (this.typeIsCorrect(object)) {
                final Option<Void> result = aCase.unapply(object);
                if (!result.isNone()) {
                    return this.function.apply();
                }
            }
            throw new MatchingException();
        }
    }

    private class Rule1<M> extends Rule {
        private final Case1<T, M> aCase;
        private final Function1<M, R> function;

        private Rule1(Class<T> type, Case1<T, M> aCase, Function1<M, R> function) {
            super(type);
            this.aCase = aCase;
            this.function = function;
        }

        R apply(T object) throws MatchingException {
            if (this.typeIsCorrect(object)) {
                final Option<M> result = aCase.unapply(object);
                if (!result.isNone()) {
                    return this.function.apply(result.value());
                }
            }
            throw new MatchingException();
        }
    }

    private class Rule2<M1, M2> extends Rule {
        private final Case2<T, M1, M2> aCase;
        private final Function2<M1, M2, R> function;

        private Rule2(Class<T> type, Case2<T, M1, M2> aCase, Function2<M1, M2, R> function) {
            super(type);
            this.aCase = aCase;
            this.function = function;
        }

        R apply(T object) throws MatchingException {
            if (this.typeIsCorrect(object)) {
                final Option<Tuple2<M1, M2>> result = aCase.unapply(object);
                if (!result.isNone()) {
                    return this.function.apply(result.value()._1, result.value()._2);
                }
            }
            throw new MatchingException();
        }
    }

    private class Rule3<M1, M2, M3> extends Rule {
        private final Case3<T, M1, M2, M3> aCase;
        private final Function3<M1, M2, M3, R> function;

        private Rule3(Class<T> type, Case3<T, M1, M2, M3> aCase, Function3<M1, M2, M3, R> function) {
            super(type);
            this.aCase = aCase;
            this.function = function;
        }

        R apply(T object) throws MatchingException {
            if (this.typeIsCorrect(object)) {
                final Option<Tuple3<M1, M2, M3>> result = aCase.unapply(object);
                if (!result.isNone()) {
                    return this.function.apply(result.value()._1, result.value()._2, result.value()._3);
                }
            }
            throw new MatchingException();
        }
    }

    // =================================================================================================================

    public class When0 {
        private final Class<T> type;
        private final Case0<T> aCase;

        public When0(Class<T> type, Case0<T> aCase) {
            this.type = type;
            this.aCase = aCase;
        }

        public Match<T, R> then(R result) {
            return then(Functions.<R>constant0(result));
        }

        public Match<T, R> then(Function0<R> function) {
            Match.this.rules.add(new Rule0(type, aCase, function));
            return Match.this;
        }
    }

    public class When1<M> {
        private final Class<T> type;
        private final Case1<T, M> aCase;

        public When1(Class<T> type, Case1<T, M> aCase) {
            this.type = type;
            this.aCase = aCase;
        }

        public Match<T, R> then(R result) {
            return then(Functions.<M, R>constant1(result));
        }

        public Match<T, R> then(Function1<M, R> function) {
            Match.this.rules.add(new Rule1<>(type, aCase, function));
            return Match.this;
        }
    }

    public class When2<M1, M2> {
        private final Class<T> type;
        private final Case2<T, M1, M2> aCase;

        public When2(Class<T> type, Case2<T, M1, M2> aCase) {
            this.type = type;
            this.aCase = aCase;
        }

        public Match<T, R> then(R result) {
            return then(Functions.<M1, M2, R>constant2(result));
        }

        public Match<T, R> then(Function2<M1, M2, R> function) {
            Match.this.rules.add(new Rule2<>(type, aCase, function));
            return Match.this;
        }
    }


    public class When3<M1, M2, M3> {
        private final Class<T> type;
        private final Case3<T, M1, M2, M3> aCase;

        public When3(Class<T> type, Case3<T, M1, M2, M3> aCase) {
            this.type = type;
            this.aCase = aCase;
        }

        public Match<T, R> then(R result) {
            return then(Functions.<M1, M2, M3, R>constant3(result));
        }

        public Match<T, R> then(Function3<M1, M2, M3, R> function) {
            Match.this.rules.add(new Rule3<>(type, aCase, function));
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

    public When0 when(final Case0<T> aCase) {
        return new When0(this.<T>getType(aCase), aCase);
    }

    public When1<?> when(final T object) {
        return when1(Cases.<T>reify(object));
    }

    public When1<?> when(final Cases.AnyValueObject object) {
        return when1(Cases.<T>reify(object));
    }

    public <M> When1<M> when(final Case1<T, M> aCase) {
        return when1(aCase);
    }

    private <M> When1<M> when1(final Case1<T, M> aCase) {
        if (aCase == null) {
            return new When1(null, Cases.<T>nil());
        } else {
            return new When1(this.<T>getType(aCase), aCase);
        }
    }

    public <M1, M2> When2<M1, M2> when(final Case2<T, M1, M2> aCase) {
        return new When2(this.<T>getType(aCase), aCase);
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