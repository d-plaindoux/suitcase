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
import org.smallibs.suitcase.utils.Function2;
import org.smallibs.suitcase.utils.Pair;

import static org.smallibs.suitcase.cases.core.Cases.__;
import static org.smallibs.suitcase.cases.core.Cases.constant;
import static org.smallibs.suitcase.cases.core.Cases.var;
import static org.smallibs.suitcase.cases.utils.Pairs.Pair;

public class SimpleMatcherTest {

    @Test
    public void shouldMatchNullObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(constant(null)).then(42);

        TestCase.assertEquals(42, matcher.match(null).intValue());
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchConstantObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(constant(null)).then(42);

        matcher.match(19);
    }

    interface A {
    }

    static class B implements A {
    }

    static class C implements A {
    }

    @Test
    public void shouldMatchASubclasses() throws MatchingException {
        final Matcher<A, String> matcherA = Matcher.create();

        matcherA.caseOf(var.<A>of(B.class)).then(acceptor -> "B");
        matcherA.caseOf(C.class).then("C");
        matcherA.caseOf(__).then("A");

        TestCase.assertEquals("B", matcherA.match(new B()));
        TestCase.assertEquals("C", matcherA.match(new C()));
    }

    @Test
    public void shouldSwitchPair() throws MatchingException {
        final Matcher<Pair<Integer, String>, Pair<String, Integer>> matcher = Matcher.create();

        matcher.caseOf(Pair(var, var)).then(new Function2<Integer, String, Pair<String, Integer>>() {
            public Pair<String, Integer> apply(Integer i, String s) {
                return new Pair<>(s, i);
            }
        });

        TestCase.assertEquals(new Pair<>("a", 1), matcher.match(new Pair<>(1, "a")));
    }
}
