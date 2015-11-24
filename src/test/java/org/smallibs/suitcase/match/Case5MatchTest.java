/*
 *
 *  * Copyright (C)2013 D. Plaindoux.
 *  *
 *  * This program is free software; you can redistribute it and/or modify it
 *  * under the terms of the GNU Lesser General Public License as published
 *  * by the Free Software Foundation; either version 2, or (at your option) any
 *  * later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with this program; see the file COPYING.  If not, write to
 *  * the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.core.Case5;
import org.smallibs.suitcase.cases.core.TypeCase;

import static org.smallibs.suitcase.cases.core.Cases.Constant;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.utils.Functions.function;

public class Case5MatchTest {

    private final Case5<C, C, Integer, Integer, Integer, Integer, Integer> CC = TypeCase.of(C.class, p -> p.i1, p -> p.i2, p -> p.i3, p -> p.i4, p -> p.i5);

    @Test
    public void shouldMatchCVVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(), Var(), Var(), Var())).then(function((a, b, c, d) -> a + b + c + d == 14));

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCCCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Constant(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCCVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(), Constant(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCVCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(), Constant(3), Constant(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCCCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Var(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCVVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(), Var(), Constant(4), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCCCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Constant(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCVVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Constant(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCVCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Var(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCVCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Constant(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCCVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Var(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCCVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Constant(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCCCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Var(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCVCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Var(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCCVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Var(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchCVVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Var(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVVVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(), Var(), Var(), Var())).then(function((a, b, c, d, e) -> a + b + c + d + e == 15));

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVCCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Constant(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVCVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(), Constant(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVVCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(), Constant(3), Constant(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVCCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Var(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVVVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(), Var(), Constant(4), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVCCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Constant(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVVVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(3), Constant(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVVCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Var(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVVCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Constant(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVCVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Var(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVCVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Constant(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVCCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Var(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVVCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Var(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVCVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Var(4), Var(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    @Test
    public void shouldMatchVVVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(3), Var(4), Constant(5))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5)));
    }

    //
    // Class definition
    //

    public static class C {
        private final int i1;
        private final int i2;
        private final int i3;
        private final int i4;
        private final int i5;

        public C(int i1, int i2, int i3, int i4, int i5) {
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
            this.i4 = i4;
            this.i5 = i5;
        }
    }


}
