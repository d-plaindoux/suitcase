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

import static org.smallibs.suitcase.match.cases.PeanoTest.Succ;
import static org.smallibs.suitcase.match.cases.PeanoTest.Zero;
import static org.smallibs.suitcase.cases.core.Cases.__;
import static org.smallibs.suitcase.cases.core.Cases.var;

public class PeanoMatcherTest {

    @Test
    public void shouldCheckZero() throws Exception {

        Matcher<Integer, Boolean> isZero = Matcher.create();

        isZero.caseOf(Zero).then(true);
        isZero.caseOf(Succ(__)).then(false);

        TestCase.assertEquals((boolean) isZero.match(0), true);
        TestCase.assertEquals((boolean) isZero.match(1), false);
    }

    @Test
    public void shouldMatchExtractedInteger() throws Exception {
        final Matcher<Integer, Integer> nextInteger = Matcher.create();

        nextInteger.caseOf(Zero).then(1);
        nextInteger.caseOf(Succ(Succ(var()))).then(i -> (i + 1));

        TestCase.assertEquals((int) nextInteger.match(0), 1);
        TestCase.assertEquals((int) nextInteger.match(1), 2);
    }
}
