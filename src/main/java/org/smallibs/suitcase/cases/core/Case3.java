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

import org.smallibs.suitcase.cases.Case.WithCapture;
import org.smallibs.suitcase.cases.Case.WithoutCapture;
import org.smallibs.suitcase.cases.Result;
import org.smallibs.suitcase.utils.Pair;

import java.util.Optional;
import java.util.function.Function;

;

public class Case3<P, R, E1, E2, E3> {

    private final Function<P, Optional<R>> predicate;
    private final Function<P, E1> compute1;
    private final Function<P, E2> compute2;
    private final Function<P, E3> compute3;

    public Case3(Function<P, Optional<R>> predicate, Function<P, E1> compute1, Function<P, E2> compute2, Function<P, E3> compute3) {
        this.predicate = predicate;
        this.compute1 = compute1;
        this.compute2 = compute2;
        this.compute3 = compute3;
    }

    public <C1, C2, C3> WithoutCapture<P, Pair<C1, Pair<C2, C3>>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2, C2> aCase2, WithoutCapture<E3, C3> aCase3) {
        final Case2<P, R, E1, Pair<E2, E3>> case2 =
                new Case2<>(predicate, compute1, (p -> new Pair<>(compute2.apply(p), compute3.apply(p))));

        final WithoutCapture<Pair<E2, E3>, Pair<C2, C3>> capture = e2e3 ->
                aCase2.unapply(e2e3._1).
                        flatMap(c2 -> aCase3.unapply(e2e3._2).
                                map(c3 -> Result.success(new Pair<>(c2.resultValue(), c3.resultValue()))));

        return case2.$(aCase1, capture);
    }

    public <C1, C2, C3> WithCapture<P, C1> $(WithCapture<E1, C1> aCase1, WithoutCapture<E2, C2> aCase2, WithoutCapture<E3, C3> aCase3) {
        final Case2<P, R, E1, Pair<E2, E3>> case2 =
                new Case2<>(predicate, compute1, (p -> new Pair<>(compute2.apply(p), compute3.apply(p))));

        final WithoutCapture<Pair<E2, E3>, Pair<C2, C3>> capture = e2e3 ->
                aCase2.unapply(e2e3._1).
                        flatMap(c2 -> aCase3.unapply(e2e3._2).
                                map(c3 -> Result.success(new Pair<>(c2.resultValue(), c3.resultValue()))));

        return case2.$(aCase1, capture);
    }

    public <C1, C2, C3> WithCapture<P, C2> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2, C2> aCase2, WithoutCapture<E3, C3> aCase3) {
        final Case2<P, R, E1, Pair<E2, E3>> case2 =
                new Case2<>(predicate, compute1, (p -> new Pair<>(compute2.apply(p), compute3.apply(p))));

        final WithCapture<Pair<E2, E3>, C2> capture = e2e3 ->
                aCase2.unapply(e2e3._1).flatMap(c2 -> aCase3.unapply(e2e3._2).map(c3 -> c2));

        return case2.$(aCase1, capture);
    }

    public <C1, C2, C3> WithCapture<P, C3> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2, C2> aCase2, WithCapture<E3, C3> aCase3) {
        final Case2<P, R, E1, Pair<E2, E3>> case2 =
                new Case2<>(predicate, compute1, (p -> new Pair<>(compute2.apply(p), compute3.apply(p))));

        final WithCapture<Pair<E2, E3>, C3> capture = e2e3 ->
                aCase2.unapply(e2e3._1).flatMap(c2 -> aCase3.unapply(e2e3._2));

        return case2.$(aCase1, capture);
    }

    public <C1, C2, C3> WithCapture<P, Pair<C1, C2>> $(WithCapture<E1, C1> aCase1, WithCapture<E2, C2> aCase2, WithoutCapture<E3, C3> aCase3) {
        final Case2<P, R, E1, Pair<E2, E3>> case2 =
                new Case2<>(predicate, compute1, (p -> new Pair<>(compute2.apply(p), compute3.apply(p))));

        final WithCapture<Pair<E2, E3>, C2> capture = e2e3 ->
                aCase2.unapply(e2e3._1).flatMap(c2 -> aCase3.unapply(e2e3._2).map(c3 -> c2));

        return case2.$(aCase1, capture);
    }

    public <C1, C2, C3> WithCapture<P, Pair<C1, C3>> $(WithCapture<E1, C1> aCase1, WithoutCapture<E2, C2> aCase2, WithCapture<E3, C3> aCase3) {
        final Case2<P, R, E1, Pair<E2, E3>> case2 =
                new Case2<>(predicate, compute1, (p -> new Pair<>(compute2.apply(p), compute3.apply(p))));

        final WithCapture<Pair<E2, E3>, C3> capture = e2e3 ->
                aCase2.unapply(e2e3._1).flatMap(c2 -> aCase3.unapply(e2e3._2));

        return case2.$(aCase1, capture);
    }

    public <C1, C2, C3> WithCapture<P, Pair<C2, C3>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2, C2> aCase2, WithCapture<E3, C3> aCase3) {
        final Case2<P, R, E1, Pair<E2, E3>> case2 =
                new Case2<>(predicate, compute1, (p -> new Pair<>(compute2.apply(p), compute3.apply(p))));

        final WithCapture<Pair<E2, E3>, Pair<C2, C3>> capture = e2e3 ->
                aCase2.unapply(e2e3._1).
                        flatMap(c2 -> aCase3.unapply(e2e3._2).
                                map(c3 -> Result.successWithCapture(new Pair<>(c2.resultValue(), c3.resultValue()))));

        return case2.$(aCase1, capture);
    }

    public <C1, C2, C3> WithCapture<P, Pair<C1, Pair<C2, C3>>> $(WithCapture<E1, C1> aCase1, WithCapture<E2, C2> aCase2, WithCapture<E3, C3> aCase3) {
        final Case2<P, R, E1, Pair<E2, E3>> case2 =
                new Case2<>(predicate, compute1, (p -> new Pair<>(compute2.apply(p), compute3.apply(p))));

        final WithCapture<Pair<E2, E3>, Pair<C2, C3>> capture = e2e3 ->
                aCase2.unapply(e2e3._1).
                        flatMap(c2 -> aCase3.unapply(e2e3._2).
                                map(c3 -> Result.successWithCapture(new Pair<>(c2.resultValue(), c3.resultValue()))));

        return case2.$(aCase1, capture);
    }
}
