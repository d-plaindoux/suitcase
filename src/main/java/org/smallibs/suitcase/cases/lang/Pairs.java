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

package org.smallibs.suitcase.cases.lang;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.core.Case2;
import org.smallibs.suitcase.cases.core.Cases;
import org.smallibs.suitcase.utils.Pair;

import java.util.Optional;

public interface Pairs {

    static <T1, T2> Case2<Pair<T1, T2>, Pair<T1, T2>, T1, T2> Pair() {
        return new Case2<>(
                Optional::of,
                l -> l._1,
                l -> l._2
        );
    }

    static <T1, T2, C2> Case.WithoutCapture<Pair<T1, T2>, Pair<T1, C2>> Pair(T1 aCase1, Case.WithoutCapture<T2, C2> aCase2) {
        return Pair(Cases.Constant(aCase1), aCase2);
    }

    static <T1, T2, C2> Case.WithCapture<Pair<T1, T2>, C2> Pair(T1 aCase1, Case.WithCapture<T2, C2> aCase2) {
        return Pair(Cases.Constant(aCase1), aCase2);
    }

    static <T1, T2, C1> Case.WithoutCapture<Pair<T1, T2>, Pair<C1, T2>> Pair(Case.WithoutCapture<T1, C1> aCase1, T2 aCase2) {
        return Pair(aCase1, Cases.Constant(aCase2));
    }

    static <T1, T2, C1> Case.WithCapture<Pair<T1, T2>, C1> Pair(Case.WithCapture<T1, C1> aCase1, T2 aCase2) {
        return Pair(aCase1, Cases.Constant(aCase2));
    }

    static <T1, T2, C1, C2> Case.WithoutCapture<Pair<T1, T2>, Pair<C1, C2>> Pair(Case.WithoutCapture<T1, C1> aCase1, Case.WithoutCapture<T2, C2> aCase2) {
        return Pairs.<T1, T2>Pair().$(aCase1, aCase2);
    }

    static <T1, T2, C1, C2> Case.WithCapture<Pair<T1, T2>, C1> Pair(Case.WithCapture<T1, C1> aCase1, Case.WithoutCapture<T2, C2> aCase2) {
        return Pairs.<T1, T2>Pair().$(aCase1, aCase2);
    }

    static <T1, T2, C1, C2> Case.WithCapture<Pair<T1, T2>, C2> Pair(Case.WithoutCapture<T1, C1> aCase1, Case.WithCapture<T2, C2> aCase2) {
        return Pairs.<T1, T2>Pair().$(aCase1, aCase2);
    }

    static <T1, T2, C1, C2> Case.WithCapture<Pair<T1, T2>, Pair<C1, C2>> Pair(Case.WithCapture<T1, C1> aCase1, Case.WithCapture<T2, C2> aCase2) {
        return Pairs.<T1, T2>Pair().$(aCase1, aCase2);
    }
}
