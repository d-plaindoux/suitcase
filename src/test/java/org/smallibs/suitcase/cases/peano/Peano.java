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

package org.smallibs.suitcase.cases.peano;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;

import java.util.Optional;

import java.util.ArrayList;

import static org.smallibs.suitcase.cases.core.Cases.constant;

public final class Peano {

    public static Zero Zero = new Zero();

    public static Succ Succ(Integer o) {
        return new Succ(constant(o));
    }
    public static Succ Succ(Case<Integer> o) {
        return new Succ(o);
    }

    @CaseType(Integer.class)
    public static class Zero implements Case<Integer> {

        @Override
        public Optional<MatchResult> unapply(Integer integer) {
            if (integer == 0) {
                return Optional.ofNullable(new MatchResult(integer));
            } else {
                return Optional.empty();
            }
        }
    }

    @CaseType(Integer.class)
    public static class Succ implements Case<Integer> {

        private final Case<Integer> value;

        public Succ(Case<Integer> o1) {
            this.value = o1;
        }

        @Override
        public Optional<MatchResult> unapply(Integer integer) {
            if (integer > 0) {
                return this.value.unapply(integer - 1);
            } else {
                return Optional.empty();
            }
        }

        @Override
        public int variables() {
            return value.variables();
        }
    }
}
