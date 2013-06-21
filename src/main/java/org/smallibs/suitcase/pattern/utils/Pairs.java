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

package org.smallibs.suitcase.pattern.utils;

import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.utils.Option;
import org.smallibs.suitcase.utils.Pair;

import java.util.LinkedList;
import java.util.List;

public final class Pairs {

    public static <T1, T2> Case<Pair<T1, T2>> APair(Object o1, Object o2) {
        return new Of<>(o1, o2);
    }

    static class Of<T1, T2> implements Case<Pair<T1, T2>> {

        private final Case<T1> c1;
        private final Case<T2> c2;

        public Of(Object o1, Object o2) {
            this.c1 = Cases.fromObject(o1);
            this.c2 = Cases.fromObject(o2);
        }

        @Override
        public int numberOfVariables() {
            return c1.numberOfVariables() + c2.numberOfVariables();
        }

        @Override
        public Option<List<Object>> unapply(Pair<T1, T2> pair) {
            final Option<List<Object>> unapply1 = c1.unapply(pair._1);
            if (!unapply1.isNone()) {
                final Option<List<Object>> unapply2 = c2.unapply(pair._2);
                if (!unapply2.isNone()) {
                    final LinkedList<Object> objects = new LinkedList<>();
                    objects.addAll(unapply1.value());
                    objects.addAll(unapply2.value());
                    return new Option.Some<List<Object>>(objects);
                }
            }
            return new Option.None<>();
        }
    }
}
