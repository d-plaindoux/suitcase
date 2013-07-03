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
import smallibs.suitcase.utils.Function;
import smallibs.suitcase.utils.Function2;

import java.util.Arrays;
import java.util.List;

import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.core.Cases.var;
import static smallibs.suitcase.cases.utils.Lists.Cons;
import static smallibs.suitcase.cases.utils.Lists.Empty;

public class ListMatcherTest {
    @Test
    public void shouldMatchLisContainingAnObject() throws MatchingException {
        final Matcher<List<Object>, Boolean> isEmpty = Matcher.create();

        isEmpty.caseOf(Cons(1, _)).then.value(true);
        isEmpty.caseOf(_).then.value(false);

        TestCase.assertTrue(isEmpty.match(Arrays.<Object>asList(1)));
        TestCase.assertFalse(isEmpty.match(Arrays.<Object>asList()));
        TestCase.assertFalse(isEmpty.match(Arrays.<Object>asList(2, 3)));
    }

    @Test
    public void shouldComputeListSizeWithAdHocPatternObject() throws MatchingException {
        final Matcher<List<Object>, Integer> sizeOfMatcher = Matcher.create();

        sizeOfMatcher.caseOf(Empty).then.value(0);
        sizeOfMatcher.caseOf(Cons(_, var)).then.function(new Function<List<Object>, Integer>() {
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

        addAll.caseOf(Empty).then.value(0);
        addAll.caseOf(Cons(var, var)).then.function(new Function2<Integer, List<Integer>, Integer>() {
            public Integer apply(Integer i, List<Integer> l) {
                return i + addAll.match(l);
            }
        });

        TestCase.assertEquals(0, addAll.match(Arrays.<Integer>asList()).intValue());
        TestCase.assertEquals(10, addAll.match(Arrays.asList(1, 2, 3, 4)).intValue());
    }

    @Test
    public void shouldCheckIntegerAsHeadListAndReturnImplicitConstantValue() throws MatchingException {
        final Matcher<List<Integer>, Boolean> headIsZero = Matcher.create();

        headIsZero.caseOf(Cons(0, _)).then.value(true);
        headIsZero.caseOf(_).then.value(false);

        TestCase.assertTrue(headIsZero.match(Arrays.asList(0)));
        TestCase.assertFalse(headIsZero.match(Arrays.asList(1, 2)));
    }
}
