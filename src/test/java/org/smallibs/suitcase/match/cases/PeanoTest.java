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

package org.smallibs.suitcase.match.cases;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.Result;

import java.util.Optional;

import static org.smallibs.suitcase.cases.core.Cases.constant;

public class PeanoTest {

    public static Case.WithoutCapture<Integer> Zero = Case.withoutResult(new Zero());

    public static Case.WithoutCapture<Integer> Succ(int i) {
        return Succ(constant(i));
    }

    public static Case.WithoutCapture<Integer> Succ(Case.WithoutCapture<Integer> aCase) {
        return Case.withoutResult(new Succ<>(aCase));
    }

    public static <C> Case.WithCapture<Integer, C> Succ(Case.WithCapture<Integer, C> aCase) {
        return Case.withResult(new Succ<>(aCase));
    }

    static class Zero implements Case<Integer, Result.WithoutCapture> {

        @Override
        public Optional<Result.WithoutCapture> unapply(Integer integer) {
            if (integer == 0) {
                return Optional.of(Result.success());
            }

            return Optional.empty();
        }
    }

    static class Succ<C> implements Case<Integer, C> {

        private final Case<Integer, C> aCase;

        public Succ(Case<Integer, C> aCase) {
            this.aCase = aCase;
        }

        @Override
        public Optional<C> unapply(Integer integer) {
            if (integer > 0) {
                return aCase.unapply(integer);
            }

            return Optional.empty();
        }
    }

}
