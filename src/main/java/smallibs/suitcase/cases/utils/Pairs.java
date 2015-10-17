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

package smallibs.suitcase.cases.utils;

import smallibs.suitcase.annotations.CaseType;
import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.cases.core.Cases;
import java.util.Optional;
import smallibs.suitcase.utils.Pair;

import java.util.List;

public final class Pairs {

    public static <T1, T2> Case<Pair<T1, T2>> Pair(Object o1, Object o2) {
        return new PairCase<>(o1, o2);
    }

    @CaseType(Pair.class)
    private static class PairCase<T1, T2> implements Case<Pair<T1, T2>> {

        private final Case<T1> c1;
        private final Case<T2> c2;

        private PairCase(Object o1, Object o2) {
            this.c1 = Cases.fromObject(o1);
            this.c2 = Cases.fromObject(o2);
        }

        @Override
        public Optional<MatchResult> unapply(Pair<T1, T2> pair) {
            final Optional<MatchResult> unapply1 = c1.unapply(pair._1);
            if (unapply1.isPresent()) {
                final Optional<MatchResult> unapply2 = c2.unapply(pair._2);
                if (unapply2.isPresent()) {
                    return Optional.ofNullable(new MatchResult(pair).with(unapply1.get()).with(unapply2.get()));
                }
            }
            return Optional.empty();
        }

        @Override
        public List<Class> variableTypes() {
            final List<Class> classes = c1.variableTypes();
            classes.addAll(c2.variableTypes());
            return classes;
        }
    }
}
