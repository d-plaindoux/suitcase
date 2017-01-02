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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.smallibs.suitcase.cases.core.Cases.Any;
import static org.smallibs.suitcase.cases.core.Cases.Constant;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.cases.lang.Lists.Cons;
import static org.smallibs.suitcase.cases.lang.Lists.Empty;
import static org.smallibs.suitcase.utils.Functions.function;


public class ListMatcherTest {
    @Test
    public void shouldMatchLisContainingAnObject() throws MatchingException {
        final Matcher<List<Integer>, Boolean> isEmpty = Matcher.create();

        isEmpty.caseOf(Cons(Constant(1), Any())).then(true);
        isEmpty.caseOf(Any()).then(false);

        TestCase.assertTrue(isEmpty.match(Collections.singletonList(1)));
        TestCase.assertFalse(isEmpty.match(Collections.emptyList()));
        TestCase.assertFalse(isEmpty.match(Arrays.asList(2, 3)));
    }

    @Test
    public void shouldComputeListSizeWithAdHocPatternObject() throws MatchingException {
        final Matcher<List<?>, Integer> sizeOfMatcher = Matcher.create();

        sizeOfMatcher.caseOf(Empty()).then(0);
        sizeOfMatcher.caseOf(Cons(Any(), Var())).then(tail -> 1 + sizeOfMatcher.match(tail));

        TestCase.assertEquals(0, sizeOfMatcher.match(Collections.emptyList()).intValue());
        TestCase.assertEquals(4, sizeOfMatcher.match(Arrays.asList(1, 2, 3, 4)).intValue());
    }

    @Test
    public void shouldComputeAdditionWithAdHocPatternObject() throws MatchingException {
        final Matcher<List<Integer>, Integer> addAll = Matcher.create();

        addAll.caseOf(Empty()).then(0);
        addAll.caseOf(Cons(Var(), Var())).then(function((p1,p2) -> p1 + addAll.match(p2)));

        TestCase.assertEquals(0, addAll.match(Collections.emptyList()).intValue());
        TestCase.assertEquals(10, addAll.match(Arrays.asList(1, 2, 3, 4)).intValue());
    }

    @Test
    public void shouldCheckIntegerAsHeadListAndReturnImplicitConstantValue() throws MatchingException {
        final Matcher<List<Integer>, Boolean> headIsZero = Matcher.create();

        headIsZero.caseOf(Cons(Constant(0), Any())).then(true);
        headIsZero.caseOf(Any()).then(false);

        TestCase.assertTrue(headIsZero.match(Collections.singletonList(0)));
        TestCase.assertFalse(headIsZero.match(Arrays.asList(1, 2)));
    }

    @Test
    public void shouldCheckListWithOnlyOneElement() throws MatchingException {
        final Matcher<List<Integer>, Boolean> singletonList = Matcher.create();

        singletonList.caseOf(Cons(Var(), Empty())).then(true);

        TestCase.assertTrue(singletonList.match(Collections.singletonList(0)));
    }}
