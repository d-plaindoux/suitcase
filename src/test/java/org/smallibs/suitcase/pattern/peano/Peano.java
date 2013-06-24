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

package org.smallibs.suitcase.pattern.peano;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.utils.Option;

import java.util.Arrays;
import java.util.List;

public final class Peano {

    public static Case<Integer> Zero() {
        return new Zero();
    }

    public static Case<Integer> Succ(Object o) {
        return new Succ(o);
    }

    @CaseType(Integer.class)
    public static class Zero implements Case<Integer> {

        @Override
        public int numberOfVariables() {
            return 0;
        }

        @Override
        public Option<List<Object>> unapply(Integer integer) {
            if (integer == 0) {
                return new Option.Some<>(Arrays.asList());
            } else {
                return new Option.None<>();
            }
        }

    }

    @CaseType(Integer.class)
    public static class Succ implements Case<Integer> {

        private final Case<Integer> value;

        public Succ(Object o1) {
            this.value = Cases.fromObject(o1);
        }

        @Override
        public int numberOfVariables() {
            return this.value.numberOfVariables();
        }

        @Override
        public Option<List<Object>> unapply(Integer integer) {
            if (integer > 0) {
                return this.value.unapply(integer - 1);
            } else {
                return new Option.None<>();
            }
        }
    }


}
