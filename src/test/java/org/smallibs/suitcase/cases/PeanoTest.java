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

package org.smallibs.suitcase.cases;

import org.smallibs.suitcase.cases.core.Case0;
import org.smallibs.suitcase.cases.core.Case1;

import java.util.Optional;

import static org.smallibs.suitcase.cases.core.Cases.Constant;

public class PeanoTest {

    public static Case.WithoutCapture<Integer, Integer> Zero = new Case0<Integer, Integer>(p -> p == 0 ? Optional.of(0) : Optional.empty()).$();
    public static Case1<Integer, Integer, Integer> Succ = new Case1<>(p -> p > 0 ? Optional.of(p) : Optional.empty(), p -> p - 1);

    public static Case.WithoutCapture<Integer, Integer> Succ(int i) {
        return Succ(Constant(i));
    }

    public static <C> Case.WithoutCapture<Integer, C> Succ(Case.WithoutCapture<Integer, C> aCase) {
        return Succ.$(aCase);
    }

    public static <C> Case.WithCapture<Integer, C> Succ(Case.WithCapture<Integer, C> aCase) {
        return Succ.$(aCase);
    }

}
