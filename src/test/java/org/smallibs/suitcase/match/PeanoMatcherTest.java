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

import java.util.Optional;

import static org.smallibs.suitcase.cases.core.Cases.Any;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.cases.PeanoTest.Succ;
import static org.smallibs.suitcase.cases.PeanoTest.Zero;

public class PeanoMatcherTest {

    @Test
    public void shouldCheckZero() throws Exception {

        Matcher<Integer, Boolean> isZero = Matcher.create();

        isZero.caseOf(Zero).then(true);
        isZero.caseOf(Succ(Any())).then(false);

        TestCase.assertEquals((boolean) isZero.match(0), true);
        TestCase.assertEquals((boolean) isZero.match(1), false);
    }

    @Test
    public void shouldMatchExtractedInteger() throws Exception {
        final Matcher<Integer, Optional<Integer>> minus2 = Matcher.create();

        minus2.caseOf(Succ(Succ(Var()))).then(Optional::of);
        minus2.caseOf(Any()).then(Optional.empty());


        TestCase.assertFalse(minus2.match(0).isPresent());
        TestCase.assertFalse(minus2.match(1).isPresent());
        TestCase.assertEquals((int) minus2.match(2).get(), 0);
        TestCase.assertEquals((int) minus2.match(3).get(), 1);
    }
}
