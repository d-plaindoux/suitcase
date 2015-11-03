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

import static org.smallibs.suitcase.cases.core.Cases.__;
import static org.smallibs.suitcase.cases.core.Cases.any;
import static org.smallibs.suitcase.cases.core.Cases.nil;
import static org.smallibs.suitcase.cases.core.Cases.var;

public class MatcherTest {

    @Test
    public void shouldMatchNullValue() throws Exception {
        final Matcher<Object, Boolean> isNull = Matcher.create();

        isNull.caseOf(nil()).then(true);
        isNull.caseOf(__).then(false);

        TestCase.assertTrue(isNull.match(null));
        TestCase.assertFalse(isNull.match(2));
    }

    @Test
    public void shouldMatchIntegerByType() throws Exception {
        final Matcher<Object, Boolean> isInteger = Matcher.create();

        isInteger.caseOf(Integer.class).then(true);
        isInteger.caseOf(__).then(false);

        TestCase.assertTrue(isInteger.match(0));
        TestCase.assertFalse(isInteger.match("0"));
    }

    @Test
    public void shouldMatchIntegerByTypeWithConditional() throws Exception {
        final Matcher<Object, Boolean> isInteger = Matcher.create();

        isInteger.caseOf(Integer.class).when(() -> true).then(true);
        isInteger.caseOf(__).then(false);

        TestCase.assertTrue(isInteger.match(0));
        TestCase.assertFalse(isInteger.match("0"));
    }

    @Test
    public void shouldMatchIntegerByValue() throws Exception {
        final Matcher<Integer, Boolean> isZero = Matcher.create();

        isZero.caseOf(0).then(true);
        isZero.caseOf(__).then(false);

        TestCase.assertTrue(isZero.match(0));
        TestCase.assertFalse(isZero.match(1));
    }

    @Test
    public void shouldMatchIntegerByValueAndSuppliers() throws Exception {
        final Matcher<Integer, Boolean> isZero = Matcher.create();

        isZero.caseOf(0).then(() -> true);
        isZero.caseOf(__).then(() -> false);

        TestCase.assertTrue(isZero.match(0));
        TestCase.assertFalse(isZero.match(1));
    }

    @Test
    public void shouldMatchIntegerByValueAndConditional() throws Exception {
        final Matcher<Integer, Boolean> isZero = Matcher.create();

        isZero.caseOf(var()).when(i -> i == 0).then(i -> true);
        isZero.caseOf(__).then(() -> false);

        TestCase.assertTrue(isZero.match(0));
        TestCase.assertFalse(isZero.match(1));
    }

    @Test
    public void shouldMatchIntegerByValueAndConditionalAndConstant() throws Exception {
        final Matcher<Integer, Boolean> isZero = Matcher.create();

        isZero.caseOf(var()).when(i -> i == 0).then(true);
        isZero.caseOf(__).then(false);

        TestCase.assertTrue(isZero.match(0));
        TestCase.assertFalse(isZero.match(1));
    }

    @Test
    public void shouldMatchExtractedInteger() throws Exception {
        final Matcher<Integer, Integer> isZero = Matcher.create();

        isZero.caseOf(var()).then(s -> s + 1);

        TestCase.assertEquals((int) isZero.match(0), 1);
        TestCase.assertEquals((int) isZero.match(1), 2);
    }

    @Test
    public void shouldMatchExtractedIntegerTwice() throws Exception {
        final Matcher<Integer, Integer> isZero = Matcher.create();

        isZero.caseOf(var(Integer.class)).then(s -> s + 1);

        TestCase.assertEquals((int) isZero.match(0), 1);
        TestCase.assertEquals((int) isZero.match(1), 2);
    }
}