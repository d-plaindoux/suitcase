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
import org.smallibs.suitcase.pattern.peano.Succ;
import org.smallibs.suitcase.pattern.peano.Zero;
import org.smallibs.suitcase.utils.Function;
import org.smallibs.suitcase.utils.Function2;
import org.smallibs.suitcase.utils.Pair;

import java.util.Arrays;
import java.util.List;

import static org.smallibs.suitcase.pattern.Cases._;
import static org.smallibs.suitcase.pattern.Cases.var;
import static org.smallibs.suitcase.pattern.utils.Lists.Cons;
import static org.smallibs.suitcase.pattern.utils.Lists.Empty;
import static org.smallibs.suitcase.pattern.utils.Pairs.APair;

public class MatcherTest {

    @Test
    public void shouldMatchNullObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(null).thenConstant(42);

        TestCase.assertEquals(42, matcher.match(null).intValue());
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchConstantObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(null).thenConstant(42);

        matcher.match(19);
    }

    @Test
    public void shouldMatchTypedObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(Integer.class).thenConstant(42);

        TestCase.assertEquals(42, matcher.match(0).intValue());
    }

    @Test
    public void shouldMatchWithAlternativeNullObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(Integer.class).thenConstant(42);
        matcher.caseOf(null).thenConstant(19);

        TestCase.assertEquals(42, matcher.match(0).intValue());
        TestCase.assertEquals(19, matcher.match(null).intValue());
    }

    @Test
    public void shouldMatchLisContainingAnObject() throws MatchingException {
        final Matcher<List<Object>, Boolean> isEmpty = Matcher.create();

        isEmpty.caseOf(Cons(1, _)).thenConstant(true);
        isEmpty.caseOf(_).thenConstant(false);

        TestCase.assertTrue(isEmpty.match(Arrays.<Object>asList(1)));
        TestCase.assertFalse(isEmpty.match(Arrays.<Object>asList()));
        TestCase.assertFalse(isEmpty.match(Arrays.<Object>asList(2, 3)));
    }

    @Test
    public void shouldComputeListSizeWithAdHocPatternObject() throws MatchingException {
        final Matcher<List<Object>, Integer> sizeOfMatcher = Matcher.create();

        sizeOfMatcher.caseOf(Empty()).thenConstant(0);
        sizeOfMatcher.caseOf(Cons(_, var)).then(new Function<List<Object>, Integer>() {
            public Integer apply(List<Object> tail) {
                return 1 + sizeOfMatcher.match(tail);
            }
        });

        TestCase.assertEquals(0, sizeOfMatcher.match(Arrays.<Object>asList()).intValue());
        TestCase.assertEquals(4, sizeOfMatcher.match(Arrays.<Object>asList(1, 2, 3, 4)).intValue());
    }

    @Test
    public void shouldComputeAdditionWithAdHocPatternObject() throws MatchingException {
        final Matcher<List<Integer>, Integer> addAll = Matcher.create();

        addAll.caseOf(Empty()).thenConstant(0);
        addAll.caseOf(Cons(var, var)).then(new Function2<Integer, List<Integer>, Integer>() {
            public Integer apply(Integer i, List<Integer> l) {
                return i + addAll.match(l);
            }
        });

        TestCase.assertEquals(0, addAll.match(Arrays.<Integer>asList()).intValue());
        TestCase.assertEquals(10, addAll.match(Arrays.asList(1, 2, 3, 4)).intValue());
    }

    @Test
    public void shouldComputePeanoMultiplicationWithCasePatterns() throws MatchingException {
        final Matcher<Integer, Integer> multiplyMatcher = Matcher.create();

        multiplyMatcher.caseOf(new Zero()).thenConstant(0);
        multiplyMatcher.caseOf(new Succ(var)).then(new Function<Integer, Integer>() {
            public Integer apply(Integer i) {
                return 19 + multiplyMatcher.match(i);
            }
        });

        TestCase.assertEquals(19 * 1000, multiplyMatcher.match(1000).intValue());
    }

    @Test
    public void shouldCheckEvenPeano() throws MatchingException {
        final Matcher<Integer, Boolean> evenMatcher = Matcher.create();

        evenMatcher.caseOf(0).thenConstant(true);
        evenMatcher.caseOf(new Succ(new Succ(var))).then(new Function<Integer, Boolean>() {
            public Boolean apply(Integer i) {
                return evenMatcher.match(i);
            }
        });

        evenMatcher.caseOf(_).thenConstant(false);

        TestCase.assertEquals(true, evenMatcher.match(10).booleanValue());
    }

    @Test
    public void shouldCheckIntegerAndReturnImplicitConstantValue() throws MatchingException {
        final Matcher<Integer, Boolean> isZero = Matcher.create();

        isZero.caseOf(0).thenConstant(true);
        isZero.caseOf(_).thenConstant(false);

        TestCase.assertTrue(isZero.match(0));
        TestCase.assertFalse(isZero.match(1));
    }

    @Test
    public void shouldCheckIntegerAsHeadListAndReturnImplicitConstantValue() throws MatchingException {
        final Matcher<List<Integer>, Boolean> headIsZero = Matcher.create();

        headIsZero.caseOf(Cons(0, _)).thenConstant(true);
        headIsZero.caseOf(_).thenConstant(false);

        TestCase.assertTrue(headIsZero.match(Arrays.asList(0)));
        TestCase.assertFalse(headIsZero.match(Arrays.asList(1, 2)));
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

        matcherA.caseOf(var.<A>of(B.class)).then(new Function<B, String>() {
            public String apply(B acceptor) {
                return "B";
            }
        });
        matcherA.caseOf(C.class).thenConstant("C");
        matcherA.caseOf(_).thenConstant("A");

        TestCase.assertEquals("B", matcherA.match(new B()));
        TestCase.assertEquals("C", matcherA.match(new C()));
    }

    @Test
    public void shouldSwitchPair() throws MatchingException {
        final Matcher<Pair<Integer, String>, Pair<String, Integer>> matcher = Matcher.create();

        matcher.caseOf(APair(var, var)).then(new Function2<Integer, String, Pair<String, Integer>>() {
            public Pair<String, Integer> apply(Integer i, String s) {
                return new Pair<>(s, i);
            }
        });

        TestCase.assertEquals(new Pair<>("a", 1), matcher.match(new Pair<>(1, "a")));
    }
}
