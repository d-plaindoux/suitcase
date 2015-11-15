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
import org.smallibs.suitcase.cases.Result;
import org.smallibs.suitcase.utils.Pair;

import java.util.Map;

import static org.smallibs.suitcase.cases.core.Cases.Constant;

public interface Maps {

    static <T1, T2> Case.WithoutCapture<Map<T1, T2>, Pair<T1, T2>> Entry(T1 o1, T2 o2) {
        return Entry(Constant(o1), Constant(o2));
    }

    static <T1, T2, C2> Case.WithoutCapture<Map<T1, T2>, Pair<T1, C2>> Entry(T1 o1, Case.WithoutCapture<T2, C2> o2) {
        return Entry(Constant(o1), o2);
    }

    static <T1, T2, C1> Case.WithoutCapture<Map<T1, T2>, Pair<C1, T2>> Entry(Case.WithoutCapture<T1, C1> o1, T2 o2) {
        return Entry(o1, Constant(o2));
    }

    static <T1, T2, C2> Case.WithCapture<Map<T1, T2>, C2> Entry(T1 o1, Case.WithCapture<T2, C2> o2) {
        return Entry(Constant(o1), o2);
    }

    static <T1, T2, C1> Case.WithCapture<Map<T1, T2>, C1> Entry(Case.WithCapture<T1, C1> o1, T2 o2) {
        return Entry(o1, Constant(o2));
    }

    static <T1, T2, C1, C2> Case.WithoutCapture<Map<T1, T2>, Pair<C1, C2>> Entry(Case.WithoutCapture<T1, C1> o1, Case.WithoutCapture<T2, C2> o2) {
        return map -> map.keySet().stream().filter(k -> o1.unapply(k).isPresent()).findFirst().
                flatMap(k -> o2.unapply(map.get(k)).map(c2 -> Result.success(new Pair<>(o1.unapply(k).get().resultValue(), c2.resultValue()))));
    }

    static <T1, T2, C1, C2> Case.WithCapture<Map<T1, T2>, C1> Entry(Case.WithCapture<T1, C1> o1, Case.WithoutCapture<T2, C2> o2) {
        return map -> map.keySet().stream().filter(k -> o1.unapply(k).isPresent()).findFirst().
                flatMap(k -> o2.unapply(map.get(k)).flatMap(c2 -> o1.unapply(k)));
    }

    static <T1, T2, C1, C2> Case.WithCapture<Map<T1, T2>, C2> Entry(Case.WithoutCapture<T1, C1> o1, Case.WithCapture<T2, C2> o2) {
        return map -> map.keySet().stream().filter(k -> o1.unapply(k).isPresent()).findFirst().
                flatMap(k -> o2.unapply(map.get(k)));
    }

    static <T1, T2, C1, C2> Case.WithCapture<Map<T1, T2>, Pair<C1, C2>> Entry(Case.WithCapture<T1, C1> o1, Case.WithCapture<T2, C2> o2) {
        return map -> map.keySet().stream().filter(k -> o1.unapply(k).isPresent()).findFirst().
                flatMap(k -> o2.unapply(map.get(k)).map(c2 -> Result.successWithCapture(new Pair<>(o1.unapply(k).get().resultValue(), c2.resultValue()))));
    }

}
