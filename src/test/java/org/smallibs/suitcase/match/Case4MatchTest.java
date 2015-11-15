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

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.core.Case3;
import org.smallibs.suitcase.cases.core.Case4;
import org.smallibs.suitcase.cases.core.TypeCase;

import static org.smallibs.suitcase.cases.core.Cases.Constant;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.utils.Functions.function;

public class Case4MatchTest {

    private final Case4<C, C, Integer, Integer, Integer, Integer> CC = TypeCase.of(C.class, p -> p.i1, p -> p.i2, p -> p.i3, p -> p.i4);

    @Test
    public void shouldMatchVVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Var(), Var(), Var())).then(function((a,b,c,d) -> a + b + c + d == 10));

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchCCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchCVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(), Constant(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchVCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Constant(2), Constant(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }
    
    @Test
    public void shouldMatchCCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchVVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Var(), Constant(3), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchCCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Var(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchVVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchVCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchVCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Var(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchCVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchCVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Var(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchCCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Var(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchVCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Var(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchCVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Var(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    //
    // Class definition
    //

    public static class C {
        private final int i1;
        private final int i2;
        private final int i3;
        private final int i4;

        public C(int i1, int i2, int i3, int i4) {
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
            this.i4 = i4;
        }
    }


}
