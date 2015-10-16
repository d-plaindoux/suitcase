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

package smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import smallibs.suitcase.utils.Function;
import smallibs.suitcase.utils.Pair;

import static smallibs.suitcase.cases.core.Cases.__;
import static smallibs.suitcase.cases.core.Cases.var;
import static smallibs.suitcase.cases.lang.Beans.Att;
import static smallibs.suitcase.cases.lang.Beans.Obj;
import static smallibs.suitcase.cases.utils.Pairs.Pair;

public class PairMatcherTest {

    @Test
    public void shouldMatchAPair() {
        final Matcher<Object, Boolean> matcher = Matcher.create();

        matcher.caseOf(Pair(__,__)).then(true);

        TestCase.assertTrue(matcher.match(new Pair<>(1,2)));
    }

    @Test
    public void shouldMatchAPairAndGetFirst() {
        final Matcher<Pair<Integer,String>, Integer> matcher = Matcher.create();

        matcher.caseOf(Pair(var,__)).then((Integer i) -> i);

        TestCase.assertEquals(matcher.match(new Pair<>(1, "2")), new Integer(1));
    }

    @Test
    public void shouldMatchAPairAndGetSecond() {
        final Matcher<Pair<Integer,String>, String> matcher = Matcher.create();

        matcher.caseOf(Pair(__,var)).then((String i) -> i);

        TestCase.assertEquals(matcher.match(new Pair<>(1, "2")), "2");
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchAPairBecauseOfTheFirst() {
        final Matcher<Object, Boolean> matcher = Matcher.create();

        matcher.caseOf(Pair(2,__)).then(true);

        matcher.match(new Pair<>(1,"2"));
    }

    @Test(expected = MatchingException.class)
    public void shouldNotMatchAPairBecauseOfTheSecond() {
        final Matcher<Object, Boolean> matcher = Matcher.create();

        matcher.caseOf(Pair(__,"3")).then(true);

        matcher.match(new Pair<>(1,"2"));
    }
}
