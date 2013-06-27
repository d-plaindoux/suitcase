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

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.utils.Option;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class Lists {

    public static Case<List<?>> Empty = new Empty();

    public static <T> Case<List<T>> Cons(Object o1, Object o2) {
        return new Cons<>(o1, o2);
    }

    @CaseType(List.class)
    private static class Empty<E> implements Case<List<E>> {

        @Override
        public Option<List<Object>> unapply(List<E> list) {
            if (list.isEmpty()) {
                return new Option.Some<>(Arrays.asList());
            } else {
                return new Option.None<>();
            }
        }
    }

    @CaseType(List.class)
    private static class Cons<E> implements Case<List<E>> {

        private final Case<E> caseHead;
        private final Case<List<E>> caseTail;

        private Cons(Object o1, Object o2) {
            this.caseHead = Cases.fromObject(o1);
            this.caseTail = Cases.fromObject(o2);
        }

        @Override
        public Option<List<Object>> unapply(List<E> list) {
            if (!list.isEmpty()) {
                final List<E> tail = new LinkedList<>(list);
                final Option<List<Object>> headResult = this.caseHead.unapply(tail.remove(0));

                if (!headResult.isNone()) {
                    final Option<List<Object>> tailResult = this.caseTail.unapply(tail);
                    if (!tailResult.isNone()) {
                        final List<Object> result = new LinkedList<>();
                        result.addAll(headResult.value());
                        result.addAll(tailResult.value());
                        return new Option.Some<>(result);
                    }
                }
            }

            return new Option.None<>();
        }

    }
}
