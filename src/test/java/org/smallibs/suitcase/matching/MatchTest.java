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
import org.smallibs.suitcase.callback.CallBack;
import org.smallibs.suitcase.pattern.core.Cases;
import org.smallibs.suitcase.pattern.list.Cons;
import org.smallibs.suitcase.pattern.list.Nil;
import org.smallibs.suitcase.pattern.peano.Succ;
import org.smallibs.suitcase.pattern.peano.Zero;
import org.smallibs.suitcase.utils.Couple;

import java.util.Arrays;
import java.util.List;

public class MatchTest {

    @Test
    public void shouldMatchNullObject() throws MatchingException {
        final Match<Object, Integer> match = Match.<Object, Integer>match().
                when(null).then(42);

        TestCase.assertEquals(42, match.apply(null).intValue());
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchConstantObject() throws MatchingException {
        final Match<Object, Integer> match = Match.<Object, Integer>match().
                when(null).then(42);

        match.apply(19);
    }

    @Test
    public void shouldMatchTypedObject() throws MatchingException {
        final Match<Object, Integer> match = Match.<Object, Integer>match().
                when(Integer.class).then(42);

        TestCase.assertEquals(42, match.apply(0).intValue());
    }

    @Test
    public void shouldMatchWithAlternativeNullObject() throws MatchingException {
        final Match<Object, Integer> match = Match.<Object, Integer>match().
                when(Integer.class).then(42).
                when(null).then(19);

        TestCase.assertEquals(42, match.apply(0).intValue());
        TestCase.assertEquals(19, match.apply(null).intValue());
    }

    @Test
    public void shouldMatchListWithCasesObject() throws MatchingException {
        final Match<List, Boolean> isEmpty = Match.<List, Boolean>match().
                when(new Nil(), List.class).then(true).
                when(new Cons(), List.class).then(false);

        TestCase.assertTrue(isEmpty.apply(Arrays.asList()));
        TestCase.assertFalse(isEmpty.apply(Arrays.asList(1)));
    }

    @Test
    public void shouldComputeListSizeWithAdHocPatternObject() throws MatchingException {
        final Match<List, Integer> sizeOfMatcher = Match.match();
        sizeOfMatcher.
                when(new Nil()).then(0).
                when(new Cons()).then(
                new CallBack<Couple<Object, List>, Integer>() {
                    @Override
                    public Integer apply(Couple<Object, List> couple) throws MatchingException {
                        return 1 + sizeOfMatcher.apply(couple._2);
                    }
                });

        TestCase.assertEquals(0, sizeOfMatcher.apply(Arrays.asList()).intValue());
        TestCase.assertEquals(4, sizeOfMatcher.apply(Arrays.asList(1, 2, 3, 4)).intValue());
    }

    @Test
    public void shouldComputePeanoMultiplicationWithCasePatterns() throws MatchingException {
        final Match<Integer, Integer> multiplyMatcher = Match.match();
        multiplyMatcher.
                when(new Zero()).then(0).
                when(new Succ()).then(
                new CallBack<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer i) throws MatchingException {
                        return 19 + multiplyMatcher.apply(i);
                    }
                });

        TestCase.assertEquals(19 * 1000, multiplyMatcher.apply(1000).intValue());
    }

    @Test
    public void shouldCheckEvenPeano() throws MatchingException {
        final Match<Integer, Boolean> evenMatcher = Match.match();

        evenMatcher.
                when(0).then(true).
                when(new Succ(new Succ())).then(
                new CallBack<Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer integer) throws MatchingException {
                        return evenMatcher.apply(integer);
                    }
                }).
                when(Cases.<Integer>_()).then(false);

        TestCase.assertEquals(true, evenMatcher.apply(10).booleanValue());
    }

    @Test
    public void shouldCheckIntegerAndReturnImplicitConstantValue() throws MatchingException {
        Match<Integer, Boolean> isZero = Match.<Integer, Boolean>match().
                when(0).then(true).
                when(Cases.<Integer>_()).then(false);

        TestCase.assertEquals(true, isZero.apply(0).booleanValue());
        TestCase.assertEquals(false, isZero.apply(1).booleanValue());
    }
}
