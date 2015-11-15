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

package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.Case.WithCapture;
import org.smallibs.suitcase.cases.Case.WithoutCapture;
import org.smallibs.suitcase.utils.Functions.Function2;
import org.smallibs.suitcase.utils.Pair;

import java.util.Optional;
import java.util.function.Function;

import static org.smallibs.suitcase.cases.Result.success;
import static org.smallibs.suitcase.cases.Result.successWithCapture;

public class Case2<P, R, E1, E2> {

    private final Function<P, Optional<R>> predicate;
    private final Function<P, E1> compute1;
    private final Function<P, E2> compute2;

    public Case2(Function<P, Optional<R>> predicate, Function<P, E1> compute1, Function<P, E2> compute2) {
        this.predicate = predicate;
        this.compute1 = compute1;
        this.compute2 = compute2;
    }

    public <C1, C2> WithoutCapture<P, Pair<C1, C2>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2, C2> aCase2) {
        return WithoutCapture.adapt(new Pattern<>(
                (c1, c2) -> success(new Pair<>(c1.resultValue(), c2.resultValue())),
                aCase1,
                aCase2));
    }

    public <C1, C2> WithCapture<P, C1> $(WithCapture<E1, C1> aCase1, WithoutCapture<E2, C2> aCase2) {
        return WithCapture.adapt(new Pattern<>(
                (c1, c2) -> c1,
                aCase1,
                aCase2)
        );
    }

    public <C1, C2> WithCapture<P, C2> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2, C2> aCase2) {
        return WithCapture.adapt(new Pattern<>(
                (c1, c2) -> c2,
                aCase1,
                aCase2)
        );
    }

    public <C1, C2> WithCapture<P, Pair<C1, C2>> $(WithCapture<E1, C1> aCase1, WithCapture<E2, C2> aCase2) {
        return WithCapture.adapt(new Pattern<>(
                (c1, c2) -> successWithCapture(new Pair<>(c1.resultValue(), c2.resultValue())),
                aCase1,
                aCase2)
        );
    }

    public class Pattern<C1, C2, C> implements Case<P, C> {
        private final Function2<C1, C2, C> combination;
        private final Case<E1, C1> aCase1;
        private final Case<E2, C2> aCase2;

        public Pattern(Function2<C1, C2, C> combination, Case<E1, C1> aCase1, Case<E2, C2> aCase2) {
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
