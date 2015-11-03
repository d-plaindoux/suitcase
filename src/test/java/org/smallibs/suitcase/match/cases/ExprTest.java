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
import org.smallibs.suitcase.cases.Result.Combinator;
import org.smallibs.suitcase.utils.Pair;

import java.util.Optional;

import static org.smallibs.suitcase.cases.core.Cases.constant;

public class ExprTest {

    public interface Expr {

        static Expr Nat(int i) {
            return new Nat(i);
        }

        static Expr Add(Expr l, Expr r) {
            return new Add(l, r);
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
    }

    public interface P {

        static Case.WithoutCapture<Expr> Nat(int c) {
            return Nat(constant(c));
        }

        static Case.WithoutCapture<Expr> Nat(Case.WithoutCapture<Integer> aCase) {
            return Case.withoutResult(new Nat<>(aCase));
        }

        static <C> Case.WithCapture<Expr, C> Nat(Case.WithCapture<Integer, C> aCase) {
            return Case.withResult(new Nat<>(aCase));
        }

        static Case.WithoutCapture<Expr> Add(Case.WithoutCapture<Expr> l, Case.WithoutCapture<Expr> r) {
            return Case.withoutResult(new Add<>(new Result.Without(), l, r));
        }

        static <C> Case.WithCapture<Expr, C> Add(Case.WithCapture<Expr, C> l, Case.WithoutCapture<Expr> r) {
            return Case.withResult(new Add<>(new Result.WithLeft<>(), l, r));
        }

        static <C> Case.WithCapture<Expr, C> Add(Case.WithoutCapture<Expr> l, Case.WithCapture<Expr, C> r) {
            return Case.withResult(new Add<>(new Result.WithRight<>(), l, r));
        }


        static <C1, C2> Case.WithCapture<Expr, Pair<C1, C2>> Add(Case.WithCapture<Expr, C1> l, Case.WithCapture<Expr, C2> r) {
            return Case.withResult(new Add<>(new Result.WithBoth<>(), l, r));
        }

        class Nat<C> implements Case<Expr, C> {
            private final Case<Integer, C> aCase;

            public Nat(Case<Integer, C> aCase) {
                this.aCase = aCase;
            }

            @Override
            public Optional<C> unapply(Expr expr) {
                if (expr instanceof Expr.Nat) {
                    return aCase.unapply(Expr.Nat.class.cast(expr).val);
                }

                return Optional.empty();
            }
        }

        class Add<C1, C2, C3> implements Case<Expr, C3> {
            private final Combinator<C1, C2, C3> combinator;
            private final Case<Expr, C1> leftCase;
            private final Case<Expr, C2> rightCase;

            public Add(Combinator<C1, C2, C3> combinator, Case<Expr, C1> leftCase, Case<Expr, C2> rightCase) {
                this.combinator = combinator;
                this.leftCase = leftCase;
                this.rightCase = rightCase;
            }

            @Override
            public Optional<C3> unapply(Expr expr) {
                if (expr instanceof Expr.Add) {
                    Expr.Add add = Expr.Add.class.cast(expr);
                    return leftCase.unapply(add.left).flatMap(i1 ->
                                    rightCase.unapply(add.right).map(i2 ->
                                                    combinator.apply(i1, i2)
                                    )
                    );
                }

                return Optional.empty();
            }
        }
    }
}
