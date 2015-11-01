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

package org.smallibs.suitcase.cases.utils;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;
import org.smallibs.suitcase.cases.core.Cases;
import java.util.Optional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Lists {

    public static Empty<?> Empty = new Empty<>();

    public static <T> Case<List<T>> Empty() {
        return new Empty<>();
    }

    public static <T> Case<List<T>> Cons(Object o1, Object o2) {
        return new Cons<>(o1, o2);
    }

    @CaseType(List.class)
    private static class Empty<E> implements Case<List<E>> {

        @Override
        public Optional<MatchResult> unapply(List<E> list) {
            if (list.isEmpty()) {
                return Optional.ofNullable(new MatchResult(list));
            } else {
                return Optional.empty();
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
        public Optional<MatchResult> unapply(List<E> list) {
            if (!list.isEmpty()) {
                final List<E> tail = new LinkedList<>(list);
                final Optional<MatchResult> headResult = this.caseHead.unapply(tail.remove(0));

                if (headResult.isPresent()) {
                    final Optional<MatchResult> tailResult = this.caseTail.unapply(tail);
                    if (tailResult.isPresent()) {
                        return Optional.ofNullable(new MatchResult(list).with(headResult.get()).with(tailResult.get()));
                    }
                }
            }

            return Optional.empty();
        }

        @Override
        public int variables() {
            return caseHead.variables() + caseTail.variables();
        }
    }
}
