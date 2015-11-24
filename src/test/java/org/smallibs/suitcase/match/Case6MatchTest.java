/*
 *
 *  * Copyright (C)3024 D. Plaindoux.
 *  *
 *  * This program is free software; you can redistribute it and/or modify it
 *  * under the terms of the GNU Lesser General Public License as published
 *  * by the Free Software Foundation; either version 3, or (at your option) any
 *  * later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with this program; see the file COPYING.  If not, write to
 *  * the Free Software Foundation, 676 Mass Ave, Cambridge, MA 03249, USA.
 *
 */

package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.core.Case6;
import org.smallibs.suitcase.cases.core.TypeCase;

import static org.smallibs.suitcase.cases.core.Cases.Constant;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.utils.Functions.function;

public class Case6MatchTest {

    private final Case6<C, C, Integer, Integer, Integer, Integer, Integer, Integer> CC = TypeCase.of(C.class, p -> p.i1, p -> p.i2, p -> p.i3, p -> p.i4, p -> p.i5, p -> p.i6);

    @Test
    public void shouldMatchCCVVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(), Var(), Var(), Var())).then(function((a, b, c, d) -> a + b + c + d == 18));

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCCCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Constant(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCCVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Var(), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCVCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(), Constant(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCCCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Constant(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCVVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(), Var(), Constant(5), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCCCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Constant(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCVVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Var(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCVCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Constant(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCVCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Constant(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCCVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Var(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCCVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Var(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCCCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Constant(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCVCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Constant(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCCVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Var(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCCVVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Var(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVVVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(), Var(), Var(), Var())).then(function((a, b, c, d, e) -> a + b + c + d + e == 20));

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVCCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Constant(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVCVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Var(), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVVCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(), Constant(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVCCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Constant(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVVVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(), Var(), Constant(5), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVCCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Constant(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVVVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Var(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVVCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Constant(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVVCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Constant(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVCVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Var(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVCVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Var(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVCCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Constant(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVVCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Constant(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVCVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Var(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchCVVVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3), Var(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    // MARK

    @Test
    public void shouldMatchVCVVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(), Var(), Var(), Var())).then(function((a, b, c, d, e) -> a + b + c + d + e == 19));

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCCCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Constant(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCCVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Var(), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCVCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(), Constant(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCCCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Constant(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCVVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(), Var(), Constant(5), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCCCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Constant(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCVVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Var(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCVCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Constant(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCVCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Constant(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCCVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Var(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCCVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Var(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCCCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Constant(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCVCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Constant(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCCVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Var(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVCVVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3), Var(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVVVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(), Var(), Var(), Var())).then(function((a, b, c, d, e, f) -> a + b + c + d + e + f == 21));

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVCCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Constant(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVCVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Var(), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVVCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(), Constant(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVCCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Constant(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVVVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(), Var(), Constant(5), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVCCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Constant(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVVVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(3), Var(4), Constant(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVVCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(3), Constant(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVVCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(3), Constant(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVCVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Var(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVCVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Var(4), Constant(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVCCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Constant(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVVCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(3), Constant(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVCVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Constant(3), Var(4), Var(5), Var(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void shouldMatchVVVVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Var(2), Var(3), Var(4), Var(5), Constant(6))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4, 5, 6)));
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
        private final int i6;

        public C(int i1, int i2, int i3, int i4, int i5, int i6) {
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
            this.i4 = i4;
            this.i5 = i5;
            this.i6 = i6;
        }
    }


}
