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

package smallibs.suitcase.match;

import smallibs.suitcase.annotations.CaseType;
import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.core.Cases;
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.utils.Function;
import smallibs.suitcase.utils.Function0;
import smallibs.suitcase.utils.Function2;
import smallibs.suitcase.utils.Function3;
import smallibs.suitcase.utils.Function4;
import smallibs.suitcase.utils.Functions;
import smallibs.suitcase.utils.Option;
import smallibs.suitcase.utils.Pair;

import java.util.LinkedList;
import java.util.List;

public class Matcher<T, R> {

    public static <T, R> Matcher<T, R> create() {
        return new Matcher<>();
    }

    // =================================================================================================================
    // Internal classes and intermediate code for DSL like approach
    // =================================================================================================================

    private class Rule<M> {
        private final Class<?> type;
        private final Case<T> aCase;
        private final Function<M, R> function;

        private Rule(Case<T> aCase, Function<M, R> function) {
            this.type = this.getType(aCase);
            this.aCase = aCase;
            this.function = function;
        }

        private Class<?> getType(Case aCase) {
            if (aCase.getClass().isAnnotationPresent(CaseType.class)) {
                return aCase.getClass().getAnnotation(CaseType.class).value();
            } else {
                return null;
            }
        }

        private boolean typeIsCorrect(Object object) {
            return type == null || !Cases.typeOf(type).unapply(object).isNone();
        }

        Option<MatchResult> match(T object) {
            if (this.typeIsCorrect(object)) {
                return aCase.unapply(object);
            } else {
                return new Option.None<>();
            }
        }

        Function<M, R> getFunction() {
            return function;
        }
    }

    // =================================================================================================================

    public class Then {
        private final Case<T> aCase;

        public Then(Case<T> aCase) {
            this.aCase = aCase;
        }

        public Matcher<T, R> value(R c) {
            return function(Functions.constant(c));
        }

        public Matcher<T, R> function(Function0<R> callBack) {
            rules.add(new Rule<>(aCase, Functions.<R>function(callBack)));
            return Matcher.this;
        }

        public <A> Matcher<T, R> function(Function<A, R> callBack) {
            rules.add(new Rule<>(aCase, callBack));
            return Matcher.this;
        }

        public <A, B> Matcher<T, R> function(Function2<A, B, R> callBack) {
            rules.add(new Rule<Pair<A, B>>(aCase, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A, B, C> Matcher<T, R> function(Function3<A, B, C, R> callBack) {
            rules.add(new Rule<Pair<A, Pair<B, C>>>(aCase, Functions.function(callBack)));
            return Matcher.this;
        }

        public <A, B, C, D> Matcher<T, R> function(Function4<A, B, C, D, R> callBack) {
            rules.add(new Rule<Pair<A, Pair<B, Pair<C, D>>>>(aCase, Functions.function(callBack)));
            return Matcher.this;
        }
    }

    // =================================================================================================================

    public class CaseOf {
        public final Then then;

        public CaseOf(Case<T> aCase) {
            this.then = new Then(aCase);
        }
    }

    // =================================================================================================================
    // Attributes
    // =================================================================================================================

    private final List<Rule> rules;

    // =================================================================================================================
    // Constructors
    // =================================================================================================================

    public Matcher() {
        this.rules = new LinkedList<>();
    }

    // =================================================================================================================
    // Behaviors
    // =================================================================================================================

    public CaseOf caseOf(Object object) {
        return new CaseOf(Cases.<T>fromObject(object));
    }

    private Object generateParameter(List<Object> result) {
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

    protected R reduce(final Function<Object, R> function, final List<Object> result) {
        return function.apply(generateParameter(result));
    }

    @SuppressWarnings("unchecked") // TODO
    public R match(T object) throws MatchingException {
        for (Rule rule : rules) {
            final Option<MatchResult> option = rule.match(object);
            if (!option.isNone()) {
                return this.reduce(rule.getFunction(), option.value().bindings());
            }
        }

        throw new MatchingException();
    }
}