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

package smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import smallibs.suitcase.cases.peano.Peano;
import smallibs.suitcase.utils.Function;

import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.core.Cases.var;
import static smallibs.suitcase.cases.peano.Peano.Succ;
import static smallibs.suitcase.cases.peano.Peano.Zero;

public class IntegerMatcherTest {
    @Test
    public void shouldMatchTypedObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(Integer.class).then.value(42);

        TestCase.assertEquals(42, matcher.match(0).intValue());
    }

    @Test
    public void shouldMatchWithAlternativeNullObject() throws MatchingException {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(Integer.class).then.value(42);
        matcher.caseOf(null).then.value(19);

        TestCase.assertEquals(42, matcher.match(0).intValue());
        TestCase.assertEquals(19, matcher.match(null).intValue());
    }

    @Test
    public void shouldComputePeanoMultiplicationWithCasePatterns() throws MatchingException {
        final Matcher<Integer, Integer> multiplyMatcher = Matcher.create();

        multiplyMatcher.caseOf(Peano.Zero).then.value(0);
        multiplyMatcher.caseOf(Peano.Succ(var)).then.function(new Function<Integer, Integer>() {
            public Integer apply(Integer i) {
                return 19 + multiplyMatcher.match(i);
            }
        });

        TestCase.assertEquals(19 * 1000, multiplyMatcher.match(1000).intValue());
    }

    @Test
    public void shouldCheckEvenPeano() throws MatchingException {
        final Matcher<Integer, Boolean> evenMatcher = Matcher.create();

        evenMatcher.caseOf(0).then.value(true);
        evenMatcher.caseOf(Peano.Succ(Peano.Succ(var))).then.function(new Function<Integer, Boolean>() {
            public Boolean apply(Integer i) {
                return evenMatcher.match(i);
            }
        });

        evenMatcher.caseOf(_).then.value(false);

        TestCase.assertEquals(true, evenMatcher.match(10).booleanValue());
    }

    @Test
    public void shouldCheckIntegerAndReturnImplicitConstantValue() throws MatchingException {
        final Matcher<Integer, Boolean> isZero = Matcher.create();

        isZero.caseOf(0).then.value(true);
        isZero.caseOf(_).then.value(false);

        TestCase.assertTrue(isZero.match(0));
        TestCase.assertFalse(isZero.match(1));
    }
}
