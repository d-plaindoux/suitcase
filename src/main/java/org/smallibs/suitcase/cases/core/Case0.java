package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Case.WithoutCapture;
import org.smallibs.suitcase.cases.Result;

import java.util.Optional;
import java.util.function.Function;

public class Case0<P, R> {

    private final Function<P, Optional<R>> predicate;

    public Case0(Function<P, Optional<R>> predicate) {
        this.predicate = predicate;
    }

    public WithoutCapture<P, R> $() {
        return WithoutCapture.adapt(new Pattern());
    }

    public class Pattern implements WithoutCapture<P, R> {
        public Pattern() {
        }

        @Override
        public Optional<Result.WithoutCapture<R>> unapply(P p) {
            return predicate.apply(p).flatMap(r -> Optional.of(new Result.WithoutCapture<>(r)));
        }
    }
}
