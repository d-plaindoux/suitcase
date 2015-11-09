package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.Case.WithCapture;
import org.smallibs.suitcase.cases.Case.WithoutCapture;

import java.util.Optional;
import java.util.function.Function;

public class Case1<P, R, E> {

    private final Function<P, Optional<R>> predicate;
    private final Function<R, E> compute;

    public Case1(Function<P, Optional<R>> predicate, Function<R, E> compute) {
        this.predicate = predicate;
        this.compute = compute;
    }

    public <R> WithoutCapture<P, R> $(WithoutCapture<E, R> aCase) {
        return WithoutCapture.adapt(new Pattern<>(aCase));
    }

    public <C> WithCapture<P, C> $(WithCapture<E, C> aCase) {
        return WithCapture.adapt(new Pattern<>(aCase));
    }

    public class Pattern<C> implements Case<P, C> {
        private final Case<E, C> aCase;

        public Pattern(Case<E, C> aCase) {
            this.aCase = aCase;
        }

        @Override
        public Optional<C> unapply(P e) {
            return predicate.apply(e).flatMap(r -> aCase.unapply(compute.apply(r)));
        }
    }
}