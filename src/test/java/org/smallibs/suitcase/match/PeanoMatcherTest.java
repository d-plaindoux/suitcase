package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.core.ReentrantMatcher;

import static org.junit.Assert.assertThat;
import static org.smallibs.suitcase.cases.core.Cases.var;
import static org.smallibs.suitcase.cases.core.ReentrantMatcher.reentrant;
import static org.smallibs.suitcase.cases.peano.Peano.Succ;
import static org.smallibs.suitcase.cases.peano.Peano.Zero;

public class PeanoMatcherTest {

    @Test
    public void shouldHaveStackOverflow() throws MatchingException {
        final ReentrantMatcher<Integer, String> toString = reentrant(Matcher.create());

        toString.caseOf(Zero).then("0");
        toString.caseOf(Succ(var.of(toString))).then(i -> "s(" + i + ")");

        TestCase.assertEquals(toString.match(2),"s(s(0))");
    }

}
