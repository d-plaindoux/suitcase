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
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.cases.core.Cases;
import smallibs.suitcase.utils.Function;
import smallibs.suitcase.utils.Function0;
import smallibs.suitcase.utils.Function1;
import smallibs.suitcase.utils.Function2;
import smallibs.suitcase.utils.Function3;
import smallibs.suitcase.utils.Function4;
import smallibs.suitcase.utils.Function5;
import smallibs.suitcase.utils.Function6;
import smallibs.suitcase.utils.Function7;
import smallibs.suitcase.utils.Function8;
import smallibs.suitcase.utils.Functions;
import smallibs.suitcase.utils.Option;
import smallibs.suitcase.utils.Pair;

import java.util.LinkedList;
import java.util.List;

public class Matcher<T, R> {

    public static <T, R> Matcher<T, R> create() {
        return new Matcher<>();
    }

    /**
     * Class Definition
     */

    private final List<Rule> rules;

    protected Matcher() {
        this.rules = new LinkedList<>();
    }

    public When caseOf(Object object) {
        return new When(Cases.<T>fromObject(object));
    }

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

    private <M> M apply(final Function<Object, M> function, final Object result) {
        try {
            return function.apply(result);
        } catch (Exception e) {
            throw new MatchingException(e);
        }
    }

    @SuppressWarnings("unchecked") // TODO
    public R match(T object) throws MatchingException {
        for (Rule rule : rules) {
            final Option<MatchResult> option = rule.match(object);
            if (option.isPresent()) {
                final Object o = collectParameters(option.value().bindings());
                final Function<?, Boolean> when = rule.getWhen();
                if (when == null || apply((Function<Object, Boolean>) when, o)) {
                    final Function<?, R> then = rule.getThen();
                    return this.apply((Function<Object, R>) then, o);
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
            return type == null || !Cases.typeOf(type).unapply(object).isNone();
        }

        Option<MatchResult> match(T object) {
            if (this.typeIsCorrect(object)) {
                return aCase.unapply(object);
            } else {
                return Option.None();
            }
        }

        Function<?, Boolean> getWhen() {
            return when;
        }

        Function<?, R> getThen() {
            return then;
        }
    }

    public class Then {
        protected final Function<?, Boolean> when;
        protected final Case<T> aCase;

        public Then(Case<T> aCase) {
            this.when = null;
            this.aCase = aCase;
        }

        public Then(Function<?, Boolean> when, Case<T> aCase) {
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

    public class CaseOf extends Then {
        public final Then then;

        public CaseOf(Function<?, Boolean> when, Case<T> aCase) {
            super(when, aCase);
            this.then = new Then(when, aCase);
        }
    }

    public class When extends Then {
        public final Then then;
        protected final Case<T> aCase;

        public When(Case<T> aCase) {
            super(aCase);

            this.aCase = aCase;
            this.then = new Then(aCase);
        }

        public <A> CaseOf when(Function<A, Boolean> callBack) {
            return new CaseOf(callBack, this.aCase);
        }

        public CaseOf when(Function0<Boolean> callBack) {
            return new CaseOf(Functions.function(callBack), this.aCase);
        }

        public <A> CaseOf when(Function1<A, Boolean> callBack) {
            return new CaseOf(callBack, this.aCase);
        }

        public <A, B> CaseOf when(Function2<A, B, Boolean> callBack) {
            return new CaseOf(Functions.function(callBack), this.aCase);
        }

        public <A, B, C> CaseOf when(Function3<A, B, C, Boolean> callBack) {
            return new CaseOf(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D> CaseOf when(Function4<A, B, C, D, Boolean> callBack) {
            return new CaseOf(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D, E> CaseOf when(Function5<A, B, C, D, E, Boolean> callBack) {
            return new CaseOf(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D, E, F> CaseOf when(Function6<A, B, C, D, E, F, Boolean> callBack) {
            return new CaseOf(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D, E, F, G> CaseOf when(Function7<A, B, C, D, E, F, G, Boolean> callBack) {
            return new CaseOf(Functions.function(callBack), this.aCase);
        }

        public <A, B, C, D, E, F, G, H> CaseOf when(Function8<A, B, C, D, E, F, G, H, Boolean> callBack) {
            return new CaseOf(Functions.function(callBack), this.aCase);
        }
    }
}