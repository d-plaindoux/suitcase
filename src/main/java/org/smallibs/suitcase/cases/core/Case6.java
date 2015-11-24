/*
 *
 *  * Copyright (C)3024 D. Plaindoux.
 *  *
 *  * This program is free software; you can redistribute it and/or modify it
 *  * under the terms of the GNU Lesser General Public License as published
 *  * by the Free Software Foundation; either version 3, or (at your option) any
 *  * later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with this program; see the file COPYING.  If not, write to
 *  * the Free Software Foundation, 676 Mass Ave, Cambridge, MA 03249, USA.
 *  
 */

package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Case.WithCapture;
import org.smallibs.suitcase.cases.Case.WithoutCapture;
import org.smallibs.suitcase.cases.Result;
import org.smallibs.suitcase.utils.Pair;

import java.util.Optional;
import java.util.function.Function;

;

public class Case6<P, R, E1, E2, E3, E4, E5, E6> {

    private final Function<P, Optional<R>> predicate;
    private final Function<P, E1> compute1;
    private final Function<P, E2> compute2;
    private final Function<P, E3> compute3;
    private final Function<P, E4> compute4;
    private final Function<P, E5> compute5;
    private final Function<P, E6> compute6;

    public Case6(Function<P, Optional<R>> predicate, Function<P, E1> compute1, Function<P, E2> compute2, Function<P, E3> compute3, Function<P, E4> compute4, Function<P, E5> compute5, Function<P, E6> compute6) {
        this.predicate = predicate;
        this.compute1 = compute1;
        this.compute2 = compute2;
        this.compute3 = compute3;
        this.compute4 = compute4;
        this.compute5 = compute5;
        this.compute6 = compute6;
    }

    public <C1, C2, C3, C4, C5, C6> WithoutCapture<P, Pair<C1, Pair<C2, Pair<C3, Pair<C4, Pair<C5, C6>>>>>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, C3> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, C4> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, C5> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, C6> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C3, C4>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C3, C5>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C3, C6>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C4, C5>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C4, C6>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C5, C6>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C3, Pair<C4, C5>>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C3, Pair<C4, C6>>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C3, Pair<C5, C6>>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C4, Pair<C5, C6>>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C3, Pair<C4, Pair<C5, C6>>>> $(WithoutCapture<E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, C2> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2, C3>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,C4>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,C5>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,C6>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C3, C4>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C3, C5>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C3, C6>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C4, C5>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C4, C6>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C5, C6>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C3, Pair<C4, C5>>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C3, Pair<C4, C6>>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C3, Pair<C5, C6>>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C4, Pair<C5, C6>>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C2,Pair<C3, Pair<C4, Pair<C5, C6>>>>> $(WithoutCapture<E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, C1> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,C3>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,C4>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,C5>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,C6>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C3, C4>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C3, C5>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C3, C6>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C4, C5>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C4, C6>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C5, C6>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C3, Pair<C4, C5>>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C3, Pair<C4, C6>>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C3, Pair<C5, C6>>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C4, Pair<C5, C6>>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C3, Pair<C4, Pair<C5, C6>>>>> $(WithCapture <E1, C1> aCase1, WithoutCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,C2>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2, C3>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,C4>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,C5>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,C6>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C3, C4>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithoutCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.success(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C3, C5>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C3, C6>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C4, C5>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C4, C6>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C5, C6>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C3, Pair<C4, C5>>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithoutCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C5> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> c5));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C3, Pair<C4, C6>>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithoutCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, C6> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C3, Pair<C5, C6>>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithoutCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C4, Pair<C5, C6>>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithoutCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1).flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }

    public <C1, C2, C3, C4, C5, C6> WithCapture<P, Pair<C1,Pair<C2,Pair<C3, Pair<C4, Pair<C5, C6>>>>>> $(WithCapture <E1, C1> aCase1, WithCapture<E2,C2> aCase2, WithCapture<E3, C3> aCase3, WithCapture<E4, C4> aCase4, WithCapture<E5, C5> aCase5, WithCapture<E6, C6> aCase6) {
        final Case5<P, R, E1, E2, E3, E4, Pair<E5, E6>> case5 =
                new Case5<>(predicate, compute1, compute2, compute3, compute4, (p -> new Pair<>(compute5.apply(p), compute6.apply(p))));

        final WithCapture<Pair<E5, E6>, Pair<C5, C6>> capture = e5e6 ->
                aCase5.unapply(e5e6._1)
                        .flatMap(c5 -> aCase6.unapply(e5e6._2).map(c6 -> Result.successWithCapture(new Pair<>(c5.resultValue(), c6.resultValue()))));

        return case5.$(aCase1, aCase2,  aCase3, aCase4, capture);
    }
}