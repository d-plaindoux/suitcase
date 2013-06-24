/*
 * Copyright (C)2013 D. Plaindoux.
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

package org.smallibs.suitcase.matching;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.utils.Function;
import org.smallibs.suitcase.utils.Function2;
import org.smallibs.suitcase.utils.Pair;

import static org.smallibs.suitcase.pattern.Cases._;
import static org.smallibs.suitcase.pattern.Cases.var;
import static org.smallibs.suitcase.pattern.utils.Pairs.APair;

public class SimpleMatcherTest {

    @Test
    public void shouldMatchNullObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(null).then.constant(42);

        TestCase.assertEquals(42, matcher.match(null).intValue());
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchConstantObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(null).then.constant(42);

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

        matcherA.caseOf(var.<A>of(B.class)).then.function(new Function<B, String>() {
            public String apply(B acceptor) {
                return "B";
            }
        });
        matcherA.caseOf(C.class).then.constant("C");
        matcherA.caseOf(_).then.constant("A");

        TestCase.assertEquals("B", matcherA.match(new B()));
        TestCase.assertEquals("C", matcherA.match(new C()));
    }

    @Test
    public void shouldSwitchPair() throws MatchingException {
        final Matcher<Pair<Integer, String>, Pair<String, Integer>> matcher = Matcher.create();

        matcher.caseOf(APair(var, var)).then.function(new Function2<Integer, String, Pair<String, Integer>>() {
            public Pair<String, Integer> apply(Integer i, String s) {
                return new Pair<>(s, i);
            }
        });

        TestCase.assertEquals(new Pair<>("a", 1), matcher.match(new Pair<>(1, "a")));
    }
}
