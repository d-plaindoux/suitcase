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
import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.pattern.list.Cons;
import org.smallibs.suitcase.pattern.list.Nil;
import org.smallibs.suitcase.pattern.peano.Succ;
import org.smallibs.suitcase.pattern.peano.Zero;
import org.smallibs.suitcase.utils.Function1;
import org.smallibs.suitcase.utils.Function2;

import java.util.Arrays;
import java.util.List;

import static org.smallibs.suitcase.pattern.Cases._;
import static org.smallibs.suitcase.pattern.Cases.nil;

public class MatchTest {

    @Test
    public void shouldMatchNullObject() throws MatchingException {
        final Match<Object, Integer> match = Match.match();

        match.when(nil()).then(42);

        TestCase.assertEquals(42, match.apply(null).intValue());
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchConstantObject() throws MatchingException {
        final Match<Object, Integer> match = Match.match();

        match.when(nil()).then(42);

        match.apply(19);
    }

    @Test
    public void shouldMatchTypedObject() throws MatchingException {
        final Match<Object, Integer> match = Match.match();

        match.when(Integer.class).then(42);

        TestCase.assertEquals(42, match.apply(0).intValue());
    }

    @Test
    public void shouldMatchWithAlternativeNullObject() throws MatchingException {
        final Match<Object, Integer> match = Match.match();

        match.when(Integer.class).then(42);
        match.when(nil()).then(19);

        TestCase.assertEquals(42, match.apply(0).intValue());
        TestCase.assertEquals(19, match.apply(null).intValue());
    }

    @Test
    public void shouldMatchLisContainingAnObject() throws MatchingException {
        final Match<List<Object>, Boolean> isEmpty = Match.match();

        isEmpty.when(new Cons<>(1, _)).then(true);
        isEmpty.when(_).then(false);

        TestCase.assertTrue(isEmpty.apply(Arrays.<Object>asList(1)));
        TestCase.assertFalse(isEmpty.apply(Arrays.<Object>asList()));
        TestCase.assertFalse(isEmpty.apply(Arrays.<Object>asList(2, 3)));
    }

    @Test
    public void shouldComputeListSizeWithAdHocPatternObject() throws MatchingException {
        final Match<List<Object>, Integer> sizeOfMatcher = Match.match();

        sizeOfMatcher.when(new Nil<>()).then(0);
        sizeOfMatcher.when(new Cons<>(_, _)).then(new Function2<Object, List<Object>, Integer>() {
            @Override
            public Integer apply(Object head, List<Object> tail) throws MatchingException {
                return 1 + sizeOfMatcher.apply(tail);
            }
        });

        TestCase.assertEquals(0, sizeOfMatcher.apply(Arrays.<Object>asList()).intValue());
        TestCase.assertEquals(4, sizeOfMatcher.apply(Arrays.<Object>asList(1, 2, 3, 4)).intValue());
    }

    @Test
    public void shouldComputeAdditionWithAdHocPatternObject() throws MatchingException {
        final Match<List<Integer>, Integer> addAll = Match.match();

        addAll.when(new Nil<Integer>()).then(0);
        addAll.when(new Cons<Integer>(_, _)).then(new Function2<Integer, List<Integer>, Integer>() {
            @Override
            public Integer apply(Integer i, List<Integer> l) throws MatchingException {
                return i + addAll.apply(l);
            }
        });

        TestCase.assertEquals(0, addAll.apply(Arrays.<Integer>asList()).intValue());
        TestCase.assertEquals(10, addAll.apply(Arrays.asList(1, 2, 3, 4)).intValue());
    }

    /*
    @Test
    public void shouldComputeAdditionWithAdHocPatternObjectAndTailRecursion() throws MatchingException {
        final Match<List<Integer>, Match.Rec<Integer>> addAll = Match.Rec().match(0);;

        addAll.when(new Nil<Integer>()).then(addAll);
        addAll.when(new Cons<Integer>(_, _)).then(new Function2<Integer, List<Integer>, Integer>() {
            @Override
            public TailRec<Integer> apply(Integer i, List<Integer> l) throws MatchingException {
                return deRec.accu(i + deRec.accu()).recurse(l);
            }
        });

        TestCase.assertEquals(0, addAll.apply(Arrays.<Integer>asList()).intValue());
        TestCase.assertEquals(10, addAll.apply(Arrays.asList(1, 2, 3, 4)).intValue());
    }
    */

    @Test
    public void shouldComputePeanoMultiplicationWithCasePatterns() throws MatchingException {
        final Match<Integer, Integer> multiplyMatcher = Match.match();

        multiplyMatcher.when(new Zero()).then(0);
        multiplyMatcher.when(new Succ(_)).then(new Function1<Integer, Integer>() {
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

        evenMatcher.when(0).then(true);
        evenMatcher.when(new Succ(new Succ(_))).then(
                new Function1<Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer i) throws MatchingException {
                        return evenMatcher.apply(i);
                    }
                }).
                when(Cases.<Integer>any()).then(false);

        TestCase.assertEquals(true, evenMatcher.apply(10).booleanValue());
    }

    @Test
    public void shouldCheckIntegerAndReturnImplicitConstantValue() throws MatchingException {
        Match<Integer, Boolean> isZero = Match.match();

        isZero.when(0).then(true);
        isZero.when(_).then(false);

        TestCase.assertTrue(isZero.apply(0));
        TestCase.assertFalse(isZero.apply(1));
    }

    @Test
    public void shouldCheckIntegerAsHeadListAndReturnImplicitConstantValue() throws MatchingException {
        Match<List<Integer>, Boolean> headIsZero = Match.match();

        headIsZero.when(new Cons<Integer>(0, _)).then(true);
        headIsZero.when(_).then(false);

        TestCase.assertTrue(headIsZero.apply(Arrays.asList(0)));
        TestCase.assertFalse(headIsZero.apply(Arrays.asList(1, 2)));
    }
}
