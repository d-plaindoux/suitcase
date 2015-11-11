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

import org.smallibs.suitcase.cases.Case.WithCapture;
import org.smallibs.suitcase.cases.Case.WithoutCapture;
import org.smallibs.suitcase.cases.core.Case1;
import org.smallibs.suitcase.cases.core.Case2;
import org.smallibs.suitcase.cases.core.Case3;
import org.smallibs.suitcase.cases.core.TypeCase;
import org.smallibs.suitcase.utils.Apply;
import org.smallibs.suitcase.utils.Pair;

import static org.smallibs.suitcase.cases.core.Cases.Constant;

public class ExprTest {

    public interface Expr {

        static Expr Nat(int i) {
            return new Nat(i);
        }

        static Expr Add(Expr l, Expr r) {
            return new Add(l, r);
        }

        static Expr Add3(Expr l, Expr m, Expr r) {
            return new Add3(l, m, r);
        }

        class Nat implements Expr {
            public int val;

            public Nat(int val) {
                this.val = val;
            }
        }

        class Add implements Expr {
            public Expr left, right;

            public Add(Expr left, Expr right) {
                this.left = left;
                this.right = right;
            }
        }

        class Add3 implements Expr {
            public Expr left, middle, right;

            public Add3(Expr left, Expr middle, Expr right) {
                this.left = left;
                this.middle = middle;
                this.right = right;
            }
        }
    }

    public interface P {

        Case1<Expr, Expr, Integer> Nat = TypeCase.of(Expr.Nat.class, (e -> e.val));
        Case2<Expr, Expr, Expr, Expr> Add = TypeCase.of(Expr.Add.class, (e -> e.left), (e -> e.right));
        Case3<Expr, Expr, Expr, Expr, Expr> Add3 = TypeCase.of(Expr.Add3.class, (e -> e.left), (e -> e.middle), (e -> e.right));

    }
}
