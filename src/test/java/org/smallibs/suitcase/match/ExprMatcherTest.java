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

package org.smallibs.suitcase.match;

import org.junit.Test;
import org.smallibs.suitcase.cases.ExprTest.Expr;
import org.smallibs.suitcase.cases.ExprTest.Expr.Add;
import org.smallibs.suitcase.cases.ExprTest.Expr.Nat;
import org.smallibs.suitcase.utils.Functions;

import static junit.framework.Assert.assertEquals;
import static org.smallibs.suitcase.cases.ExprTest.P.Add;
import static org.smallibs.suitcase.cases.ExprTest.P.Add3;
import static org.smallibs.suitcase.cases.ExprTest.P.Nat;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.utils.Functions.function;

public class ExprMatcherTest {

    @Test
    public void shouldEvalExpression() {
        Matcher<Expr, Integer> adder = Matcher.create();

        adder.caseOf(Var(Nat.class)).then(n -> n.val);
        adder.caseOf(Var(Add.class)).then(n -> adder.match(n.left) + adder.match(n.right));

        assertEquals((int) adder.match(Expr.Nat(1)), 1);
        assertEquals((int) adder.match(Expr.Add(Expr.Nat(1), Expr.Nat(3))), 4);
    }

    @Test
    public void shouldEvalCapturedExpression() {
        Matcher<Expr, Integer> adder = Matcher.create();

        adder.caseOf(Nat.$(Var())).then(n -> n);
        adder.caseOf(Add.$(Var(), Var())).then(Functions.function((p1, p2) -> adder.match(p1) + adder.match(p2)));

        assertEquals((int) adder.match(Expr.Nat(1)), 1);
        assertEquals((int) adder.match(Expr.Add(Expr.Nat(1), Expr.Nat(3))), 4);
    }

    @Test
    public void shouldCaptureEvaluatedExpression() {
        Matcher<Expr, Integer> adder = Matcher.create();

        adder.caseOf(Nat.$(Var())).then(n -> n);
        adder.caseOf(Add.$(Var(adder), Var(adder))).then(Functions.function((e1, e2) -> e1 + e2));
        adder.caseOf(Add3.$(Var(adder), Var(adder), Var(adder))).then(Functions.function((e1, e2, e3) -> e1 + e2 + e3));

        assertEquals((int) adder.match(Expr.Nat(1)), 1);
        assertEquals((int) adder.match(Expr.Add(Expr.Nat(1), Expr.Nat(3))), 4);
        assertEquals((int) adder.match(Expr.Add3(Expr.Nat(1), Expr.Nat(2), Expr.Nat(3))), 6);
    }
}
