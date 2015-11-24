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

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.Result;
import org.smallibs.suitcase.utils.Functions;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.smallibs.suitcase.cases.core.Cases.Constant;
import static org.smallibs.suitcase.cases.core.Cases.typeOf;

/**
 * The Matcher defines a pattern matching rule set.
 *
 * @param <T> The matched object type
 * @param <R> The matching result type
 */

public class Matcher<T, R> implements Case.WithoutCapture<T, R> {

    /**
     * The rule set
     */
    private final List<Rule<?>> rules;

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
     * Method called in order to create a new rule. The returns a When
     * object able to capture a conditional or a termination.
     *
     * @param <C> The type of the capture
     * @param object The pattern
     * @return a
     */
    public <C> WhenRuleWithoutCapture caseOf(WithoutCapture<? extends T, C> object) {
        return new WhenRuleWithoutCapture<>(object);
    }

    /**
     * Method called in order to create a new rule. The returns a When
     * object able to capture a conditional or a termination.
     *
     * @param <C> The type of the capture
     * @param object The pattern
     * @return a
     */
    public <C> WhenRuleWithCapture<C> caseOf(WithCapture<? extends T, C> object) {
        return new WhenRuleWithCapture<>(object);
    }

    /**
     * Method called in order to create a new rule. The returns a When
     * object able to capture a conditional or a termination.
     *
     * @param <E> The class type to be matched
     * @param object The pattern
     * @return a
     */
    public <E extends T> WhenRuleWithoutCapture<E> caseOf(Class<E> object) {
        return new WhenRuleWithoutCapture<>(typeOf(object));
    }

    /**
     * Method called in order to create a new rule. The returns a When
     * object able to capture a conditional or a termination.
     *
     * @param object The pattern
     * @return a
     */
    public WhenRuleWithoutCapture<T> caseOf(T object) {
        return new WhenRuleWithoutCapture<>(Constant(object));
    }

    /**
     * Main method performing the pattern matching.
     *
     * @param object The object to be matched
     * @return a computation result done by an accepted rule during pattern matching process
     * @throws MatchingException when no pattern matching rule can be applied
     */
    public R match(T object) throws MatchingException {
        for (Rule rule : rules) {
            final Optional<R> option = rule.match(object);
            if (option.isPresent()) {
                return option.get();
            }
        }

        throw new MatchingException();
    }

    // =================================================================================================================
    // Case<_,_> implementation
    // =================================================================================================================

    @Override
    public Optional<Result.WithoutCapture<R>> unapply(T t) {
        try {
            return Optional.of(Result.success(this.match(t)));
        } catch (MatchingException e) {
            return Optional.empty();
        }
    }

    // =================================================================================================================

    private abstract class Rule<O extends T> {
        abstract Optional<R> match(O object);
    }

    // =================================================================================================================
    // Behaviors for Rule Without Capture
    // =================================================================================================================

    private class RuleWithoutCapture<O extends T, C> extends Rule<O> {
        private final Case<O, Result.WithoutCapture<C>> aCase;
        private final Supplier<Boolean> when;
        private final Supplier<R> then;

        private RuleWithoutCapture(Case<O, Result.WithoutCapture<C>> aCase, Supplier<Boolean> when, Supplier<R> then) {
            this.aCase = aCase;
            this.when = when;
            this.then = then;
        }

        @Override
        Optional<R> match(O object) {
            return aCase.unapply(object).flatMap(matchResult -> {
                if (when == null || when.get()) {
                    return Optional.of(then.get());
                }

                return Optional.empty();
            });
        }
    }

    public class ThenRuleWithoutCapture<C> {
        protected final Supplier<Boolean> when;
        protected final Case<? extends T, Result.WithoutCapture<C>> aCase;

        public ThenRuleWithoutCapture(Case<? extends T, Result.WithoutCapture<C>> aCase) {
            this.when = null;
            this.aCase = aCase;
        }

        public ThenRuleWithoutCapture(Supplier<Boolean> when, Case<? extends T, Result.WithoutCapture<C>> aCase) {
            this.when = when;
            this.aCase = aCase;
        }

        public Matcher<T, R> then(R c) {
            return then(Functions.constant(c));
        }

        public Matcher<T, R> then(Supplier<R> callBack) {
            rules.add(new RuleWithoutCapture<>(aCase, when, callBack));
            return Matcher.this;
        }
    }

    public class WhenRuleWithoutCapture<C> extends ThenRuleWithoutCapture<C> {
        protected final Case<? extends T, Result.WithoutCapture<C>> aCase;

        public WhenRuleWithoutCapture(Case<? extends T, Result.WithoutCapture<C>> aCase) {
            super(aCase);

            this.aCase = aCase;
        }

        public ThenRuleWithoutCapture when(Supplier<Boolean> callBack) {
            return new ThenRuleWithoutCapture<>(callBack, this.aCase);
        }
    }


    // =================================================================================================================
    // Behaviors for Rule With Capture
    // =================================================================================================================

    private class RuleWithCapture<O extends T, C> extends Rule<O> {
        private final Case<O, Result.WithCapture<C>> aCase;
        private final Function<C, Boolean> when;
        private final Function<C, R> then;

        private RuleWithCapture(Case<O, Result.WithCapture<C>> aCase, Function<C, Boolean> when, Function<C, R> then) {
            this.aCase = aCase;
            this.when = when;
            this.then = then;
        }

        @Override
        Optional<R> match(O object) {
            return aCase.unapply(object).flatMap(matchResult -> {
                if (when == null || when.apply(matchResult.resultValue())) {
                    return Optional.of(then.apply(matchResult.resultValue()));
                }

                return Optional.empty();
            });
        }
    }

    public class ThenRuleWithCapture<C> {
        protected final Case<? extends T, Result.WithCapture<C>> aCase;
        protected final Function<C, Boolean> when;

        public ThenRuleWithCapture(Case<? extends T, Result.WithCapture<C>> aCase) {
            this.when = null;
            this.aCase = aCase;
        }

        public ThenRuleWithCapture(Function<C, Boolean> when, Case<? extends T, Result.WithCapture<C>> aCase) {
            this.when = when;
            this.aCase = aCase;
        }

        public Matcher<? extends T, R> then(Function<C, R> callBack) {
            rules.add(new RuleWithCapture<>(aCase, when, callBack));
            return Matcher.this;
        }

        public Matcher<? extends T, R> then(R callBack) {
            rules.add(new RuleWithCapture<>(aCase, when, (c -> callBack)));
            return Matcher.this;
        }
    }

    public class WhenRuleWithCapture<C> extends ThenRuleWithCapture<C> {
        protected final Case<? extends T, Result.WithCapture<C>> aCase;

        public WhenRuleWithCapture(Case<? extends T, Result.WithCapture<C>> aCase) {
            super(aCase);

            this.aCase = aCase;
        }

        public ThenRuleWithCapture<C> when(Function<C, Boolean> callBack) {
            return new ThenRuleWithCapture<>(callBack, this.aCase);
        }
    }

}