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
import org.smallibs.suitcase.utils.Pair;

import java.util.function.Function;

import static java.util.function.Function.identity;
import static org.smallibs.suitcase.cases.core.Cases.Any;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.cases.lang.Pairs.Pair;

public class PairMatcherTest {

    @Test
    public void shouldMatchAPair() {
        final Matcher<Object, Boolean> matcher = Matcher.create();

        matcher.caseOf(Pair(Any(), Any())).then(true);

        TestCase.assertTrue(matcher.match(new Pair<>(1, 2)));
    }

    @Test
    public void shouldMatchAPairAndGetFirst() {
        final Matcher<Pair<Integer, String>, Integer> matcher = Matcher.create();

        matcher.caseOf(Pair(Var(), Any())).then(e -> e);

        TestCase.assertEquals(matcher.match(new Pair<>(1, "2")), new Integer(1));
    }

    @Test
    public void shouldMatchAPairAndGetSecond() {
        final Matcher<Pair<Integer, String>, String> matcher = Matcher.create();

        matcher.caseOf(Pair(Any(), Var())).then(identity());

        TestCase.assertEquals(matcher.match(new Pair<>(1, "2")), "2");
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchAPairBecauseOfTheFirst() {
        final Matcher<Pair<Integer, Object>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Pair(2, Any())).then(true);

        matcher.match(new Pair<>(1, "2"));
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchAPairBecauseOfTheSecond() {
        final Matcher<Pair<Object, String>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Pair(Any(), "3")).then(true);

        matcher.match(new Pair<>(1, "2"));
    }
}
