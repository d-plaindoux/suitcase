package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.core.Case3;
import org.smallibs.suitcase.cases.core.Case4;
import org.smallibs.suitcase.cases.core.TypeCase;

import static org.smallibs.suitcase.cases.core.Cases.Constant;
import static org.smallibs.suitcase.cases.core.Cases.Var;

public class Case4MatchTest {

    private final Case4<C, C, Integer, Integer, Integer, Integer> CC = TypeCase.of(C.class, p -> p.i1, p -> p.i2, p -> p.i3, p -> p.i4);

    @Test
    public void shouldMatch() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Var(), Var(), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchExactly() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Constant(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchFirstAndCaptureSecond() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(), Constant(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchFirstAndCaptureExactlySecond() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2), Constant(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchSecond() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Constant(2), Constant(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }
    
    @Test
    public void shouldMatchFirstSecondAndCaptureExactlySecond() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2), Var(3), Constant(4))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }

    @Test
    public void shouldMatchThird() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Var(), Constant(3), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2, 3, 4)));
    }
    @Test
    public void shouldMatchSecondAndCaptureExactlyFirst() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2), Constant(3), Constant(4))).then(true);

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
