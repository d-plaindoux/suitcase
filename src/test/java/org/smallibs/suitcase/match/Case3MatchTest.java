package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.core.Case3;
import org.smallibs.suitcase.cases.core.TypeCase;

import static org.smallibs.suitcase.cases.core.Cases.Constant;
import static org.smallibs.suitcase.cases.core.Cases.Var;

public class Case3MatchTest {

    private final Case3<C, C, Integer, Integer, Integer> CC = TypeCase.of(C.class, p -> p.i1, p -> p.i2, p -> p.i3);

    @Test
    public void shouldMatchVVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Var(), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3)));
    }

    @Test
    public void shouldMatchCCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3)));
    }

    @Test
    public void shouldMatchCVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3)));
    }

    @Test
    public void shouldMatchCCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3)));
    }

    @Test
    public void shouldMatchVVC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Var(), Constant(3))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3)));
    }

    @Test
    public void shouldMatchVCC() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3)));
    }

    @Test
    public void shouldMatchVCV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Var(3))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3)));
    }

    @Test
    public void shouldMatchCVV() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Var(3))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3)));
    }

    //
    // Class definition
    //

    public static class C {
        private final int i1;
        private final int i2;
        private final int i3;

        public C(int i1, int i2, int i3) {
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
        }
    }


}
