package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.utils.Apply;
import org.smallibs.suitcase.utils.Pair;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Case2<P, R, E1, E2> {

    private final Function<P, Optional<R>> predicate;
    private final Function<P, E1> compute1;
    private final Function<P, E2> compute2;

    public Case2(Function<P, Optional<R>> predicate, Function<P, E1> compute1, Function<P, E2> compute2) {
        this.predicate = predicate;
        this.compute1 = compute1;
        this.compute2 = compute2;
    }

    public <C1, C2> Case.WithoutCapture<P, Pair<C1, C2>> $(Case.WithoutCapture<E1, C1> aCase1, Case.WithoutCapture<E2, C2> aCase2) {
        return Case.WithoutCapture.adapt(new Pattern<>(new Combinators.Combination2.None<>(), aCase1, aCase2));
    }

    public <C1, C2> Case.WithCapture<P, C1> $(Case.WithCapture<E1, C1> aCase1, Case.WithoutCapture<E2, C2> aCase2) {
        return Case.WithCapture.adapt(new Pattern<>(new Combinators.Combination2.With1<>(), aCase1, aCase2));
    }

    public <C1, C2> Case.WithCapture<P, C2> $(Case.WithoutCapture<E1, C1> aCase1, Case.WithCapture<E2, C2> aCase2) {
        return Case.WithCapture.adapt(new Pattern<>(new Combinators.Combination2.With2<>(), aCase1, aCase2));
    }

    public <C1, C2> Case.WithCapture<P, Apply.Apply2<C1, C2>> $(Case.WithCapture<E1, C1> aCase1, Case.WithCapture<E2, C2> aCase2) {
        return Case.WithCapture.adapt(new Pattern<>(new Combinators.Combination2.All<>(), aCase1, aCase2));
    }

    public class Pattern<C1, C2, C> implements Case<P, C> {
        private final Combinators.Combination2<C1, C2, C> combination;
        private final Case<E1, C1> aCase1;
        private final Case<E2, C2> aCase2;

        public Pattern(Combinators.Combination2<C1, C2, C> combination, Case<E1, C1> aCase1, Case<E2, C2> aCase2) {
            this.combination = combination;
            this.aCase1 = aCase1;
            this.aCase2 = aCase2;
        }

        @Override
        public Optional<C> unapply(P e) {
            return predicate.apply(e).flatMap(r ->
                    aCase1.unapply(compute1.apply(e)).flatMap(r1 ->
                            aCase2.unapply(compute2.apply(e)).map(r2 -> combination.apply(r1, r2))
                    ));
        }
    }
}
