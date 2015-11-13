package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.core.Case2;
import org.smallibs.suitcase.cases.core.TypeCase;

import static org.smallibs.suitcase.cases.core.Cases.Constant;
import static org.smallibs.suitcase.cases.core.Cases.Var;

public class Case2MatchTest {

    private final Case2<C, C, Integer, Integer> CC = TypeCase.of(C.class, p -> p.i1, p -> p.i2);

    @Test
    public void shouldMatch() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2)));
    }

    @Test
    public void shouldMatchExactly() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Constant(2))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2)));
    }

    @Test
    public void shouldMatchFirstAndCaptureSecond() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var())).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2)));
    }

    @Test
    public void shouldMatchFirstAndCaptureExactlySecond() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Constant(1), Var(2))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2)));
    }

    @Test
    public void shouldMatchSecond() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(), Constant(2))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2)));
    }

    @Test
    public void shouldMatchSecondAndCaptureExactlyFirst() throws Exception {
        Matcher<C, Boolean> matcher = Matcher.create();

        matcher.caseOf(CC.$(Var(1), Constant(2))).then(true);

        TestCase.assertTrue(matcher.match(new C(1, 2)));
    }

    //
    // Class definition
    //

    public static class C {
        private final int i1;
        private final int i2;

        public C(int i1, int i2) {
            this.i1 = i1;
            this.i2 = i2;
        }
    }


}
