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

import org.smallibs.suitcase.cases.Case.WithCapture;
import org.smallibs.suitcase.cases.Case.WithoutCapture;
import org.smallibs.suitcase.cases.core.Case0;
import org.smallibs.suitcase.cases.core.Case2;
import org.smallibs.suitcase.utils.Apply.Apply2;
import org.smallibs.suitcase.utils.Pair;

import java.util.List;
import java.util.Optional;

import static org.smallibs.suitcase.cases.core.Cases.Constant;

public interface Lists {

    static <T> WithoutCapture<List<T>, List<T>> Empty() {
        return new Case0<List<T>, List<T>>(
                p -> p.isEmpty() ? Optional.of(p) : Optional.empty()
        ).$();
    }

    static <T> Case2<List<T>, List<T>, T, List<T>> Cons() {
        return new Case2<>(
                l -> !l.isEmpty() ? Optional.of(l) : Optional.empty(),
                l -> l.get(0),
                l -> l.subList(1, l.size())
        );
    }

    static <T, C1, C2> WithoutCapture<List<T>, Pair<C1, C2>> Cons(WithoutCapture<T, C1> aCase1, WithoutCapture<List<T>, C2> aCase2) {
        return Lists.<T>Cons().$(aCase1, aCase2);
    }

    static <T, C1, C2> WithCapture<List<T>, C1> Cons(WithCapture<T, C1> aCase1, WithoutCapture<List<T>, C2> aCase2) {
        return Lists.<T>Cons().$(aCase1, aCase2);
    }

    static <T, C1, C2> WithCapture<List<T>, C2> Cons(WithoutCapture<T, C1> aCase1, WithCapture<List<T>, C2> aCase2) {
        return Lists.<T>Cons().$(aCase1, aCase2);
    }

    static <T, C1, C2> WithCapture<List<T>, Apply2<C1, C2>> Cons(WithCapture<T, C1> aCase1, WithCapture<List<T>, C2> aCase2) {
        return Lists.<T>Cons().$(aCase1, aCase2);
    }
}
