/*
 * Copyright (C)2015 D. Plaindoux.
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

package org.smallibs.suitcase.match;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;
import org.smallibs.suitcase.cases.core.Cases;
import org.smallibs.suitcase.utils.Function;
import org.smallibs.suitcase.utils.Function0;
import org.smallibs.suitcase.utils.Function1;
import org.smallibs.suitcase.utils.Function2;
import org.smallibs.suitcase.utils.Function3;
import org.smallibs.suitcase.utils.Function4;
import org.smallibs.suitcase.utils.Function5;
import org.smallibs.suitcase.utils.Function6;
import org.smallibs.suitcase.utils.Function7;
import org.smallibs.suitcase.utils.Function8;
import org.smallibs.suitcase.utils.Functions;
import org.smallibs.suitcase.utils.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The Matcher defines a pattern matching rule set.
 *
 * @param <T> The matched object type
 * @param <R> The matching result type
 */

public class Matcher<T, R> {

    /**
     * The rule set
     */
    private final List<Rule> rules;

    /**
     * The constructor
     */
    protected Matcher() {
        this.rules = new LinkedList<>();
    }

    /**
     * Factory
     *
     * @param <T> The matched object type
     * @param <R> The matching result type
     * @return a fresh pattern matching rule set
     */
    public static <T, R> Matcher<T, R> create() {
        return new Matcher<>();
    }

    /**
     * Method applying a function to a given parameters
     *
     * @param function
     * @param result
     * @param <M>
     * @return
     */
    private static <M> M apply(final Function<Object, M> function, final Object result) {
        try {
            return function.apply(result);
        } catch (Exception e) {
            throw new MatchingException(e);
        }
    }

    /**
     * Method called in order to create a new rule. The returns a When
     * object able to capture a conditional or a termination.
     *
     * @param object The pattern
     * @return a
     */
    public WhenRule caseOf(Case<? extends T> object) {
        return new WhenRule(Cases.fromObject(object));
    }

    /**
     * Method called in order to create a new rule. The returns a When
     * object able to capture a conditional or a termination.
     *
     * @param object The pattern
     * @return a
     */
    public WhenRule caseOf(Class<? extends T> object) {
        return new WhenRule(Cases.typeOf(object));
    }

    /**
     * Method called in order to create a new rule. The returns a When
     * object able to capture a conditional or a termination.
     *
     * @param object The pattern
     * @return a
     */
    public WhenRule caseOf(T object) {
        return new WhenRule(Cases.constant(object));
    }

    /**
     * Method collecting parameters building an intermediate object
     *
     * @param result The matching result
     * @return a
     */
    private Object collectParameters(List<Object> result) {
        Object parameter;

        if (result.isEmpty()) {
            parameter = null;
        } else {
            parameter = result.get(result.size() - 1);
            for (int i = result.size() - 2; i >= 0; i--) {
                parameter = new Pair<>(result.get(i), parameter);
            }
        }
        return parameter;
    }

    /**
     * Main method performing the pattern matching.
     *
     * @param object The object to be matched
     * @return a computation result done by an accepted rule during pattern matching process
     * @throws MatchingException when no pattern matching rule can be applied
     */
    @SuppressWarnings("unchecked")
    public R match(T object) throws MatchingException {
        for (Rule rule : rules) {
            final Optional<MatchResult> option = rule.match(object);

            if (option.isPresent()) {
                final Object o = collectParameters(option.get().bindings());
                if (rule.getWhen() == null || apply((Function<Object, Boolean>) rule.getWhen(), o)) {
                    return apply((Function<Object, R>) rule.getThen(), o);
                }
            }
        }

        throw new MatchingException();
    }

    // =================================================================================================================
    // Behaviors
    // =================================================================================================================

    private class Rule {
        private final Class<?> type;
        private final Case<T> aCase;
        private final Function<?, Boolean> when;
        private final Function<?, R> then;

        private Rule(Case<T> aCase, Function<?, Boolean> when, Function<?, R> then) {
            this.type = this.getType(aCase);
            this.aCase = aCase;
            this.when = when;
            this.then = then;
        }

        private Class<?> getType(Case aCase) {
            if (aCase.getClass().isAnnotationPresent(CaseType.class)) {
                return aCase.getClass().getAnnotation(CaseType.class).value();
            } else {
                return null;
            }
        }

        private boolean typeIsCorrect(Object object) {
            return type == null || Cases.typeOf(type).unapply(object).isPresent();
        }

        Optional<MatchResult> match(T object) {
            if (this.typeIsCorrect(object)) {
                return aCase.unapply(object);
            } else {
                return Optional.empty();
            }
        }

        Function<?, Boolean> getWhen() {
            return when;
        }

        Function<?, R> getThen() {
            return then;
        }
    }

    public class ThenRule {
        protected final Function<?, Boolean> when;
        protected final Case<T> aCase;

        public ThenRule(Case<T> aCase) {
            this.when = null;
            this.aCase = aCase;
        }

        public ThenRule(Function<?, Boolean> when, Case<T> aCase) {
            this.when = when;
            this.aCase = aCase;
        }

        public Matcher<T, R> then(R c) {
            return then(Functions.constant(c));
        }

        public <A> Matcher<T, R> then(Function<A, R> callBack) {
            rules.add(new Rule(aCase, when, callBack));
            return Matcher.this;
        }

        public Matcher<T, R> then(Function0<R> callBack) {
            rules.add(new Rule(aCase, when, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A> Matcher<T, R> then(Function1<A, R> callBack) {
            rules.add(new Rule(aCase, when, callBack));
            return Matcher.this;
        }

        public <A, B> Matcher<T, R> then(Function2<A, B, R> callBack) {
            rules.add(new Rule(aCase, when, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A, B, C> Matcher<T, R> then(Function3<A, B, C, R> callBack) {
            rules.add(new Rule(aCase, when, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A, B, C, D> Matcher<T, R> then(Function4<A, B, C, D, R> callBack) {
            rules.add(new Rule(aCase, when, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A, B, C, D, E> Matcher<T, R> then(Function5<A, B, C, D, E, R> callBack) {
            rules.add(new Rule(aCase, when, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A, B, C, D, E, F> Matcher<T, R> then(Function6<A, B, C, D, E, F, R> callBack) {
            rules.add(new Rule(aCase, when, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A, B, C, D, E, F, G> Matcher<T, R> then(Function7<A, B, C, D, E, F, G, R> callBack) {
            rules.add(new Rule(aCase, when, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A, B, C, D, E, F, G, H> Matcher<T, R> then(Function8<A, B, C, D, E, F, G, H, R> callBack) {
            rules.add(new Rule(aCase, when, Functions.function(callBack)));
            return Matcher.this;
        }
    }

    public class WhenRule extends ThenRule {
        protected final Case<T> aCase;

        public WhenRule(Case<T> aCase) {
            super(aCase);

            this.aCase = aCase;
        }

        public <A> ThenRule when(Function<A, Boolean> callBack) {
            return new ThenRule(callBack, this.aCase);
        }

        public ThenRule when(Function0<Boolean> callBack) {
            return new ThenRule(Functions.function(callBack), this.aCase);
        }

        public <A> ThenRule when(Function1<A, Boolean> callBack) {
            return new ThenRule(callBack, this.aCase);
        }

        public <A, B> ThenRule when(Function2<A, B, Boolean> callBack) {
            return new ThenRule(Functions.function(callBack), this.aCase);
        }

        public <A, B, C> ThenRule when(Function3<A, B, C, Boolean> callBack) {
            return new ThenRule(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D> ThenRule when(Function4<A, B, C, D, Boolean> callBack) {
            return new ThenRule(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D, E> ThenRule when(Function5<A, B, C, D, E, Boolean> callBack) {
            return new ThenRule(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D, E, F> ThenRule when(Function6<A, B, C, D, E, F, Boolean> callBack) {
            return new ThenRule(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D, E, F, G> ThenRule when(Function7<A, B, C, D, E, F, G, Boolean> callBack) {
            return new ThenRule(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D, E, F, G, H> ThenRule when(Function8<A, B, C, D, E, F, G, H, Boolean> callBack) {
            return new ThenRule(Functions.function(callBack), this.aCase);
        }
    }
}