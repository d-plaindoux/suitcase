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
import org.smallibs.suitcase.match.cases.ExprTest.Expr;
import org.smallibs.suitcase.match.cases.ExprTest.Expr.Add;
import org.smallibs.suitcase.match.cases.ExprTest.Expr.Nat;
import org.smallibs.suitcase.match.cases.ExprTest.P;

import static junit.framework.Assert.assertEquals;
import static org.smallibs.suitcase.match.cases.ExprTest.Expr.Add;
import static org.smallibs.suitcase.match.cases.ExprTest.Expr.Nat;
import static org.smallibs.suitcase.cases.core.Cases.var;

public class ExprMatcherTest {

    @Test
    public void shouldEvalExpression() throws Exception {
        Matcher<Expr, Integer> adder = Matcher.create();

        adder.caseOf(var(Nat.class)).then(n -> n.val);
        adder.caseOf(var(Add.class)).then(n -> adder.match(n.left) + adder.match(n.right));

        assertEquals((int) adder.match(Nat(1)), 1);
        assertEquals((int) adder.match(Add(Nat(1), Nat(3))), 4);
    }

    @Test
    public void shouldEvalCapturedExpression() throws Exception {
        Matcher<Expr, Integer> adder = Matcher.create();

        adder.caseOf(P.Nat(var())).then(n -> n);
        adder.caseOf(P.Add(var(), var())).then(n -> adder.match(n._1) + adder.match(n._2));

        assertEquals((int) adder.match(Nat(1)), 1);
        assertEquals((int) adder.match(Add(Nat(1), Nat(3))), 4);
    }

}
