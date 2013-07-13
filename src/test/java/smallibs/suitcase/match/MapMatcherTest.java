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

import java.util.HashMap;
import java.util.Map;

import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.utils.Maps.Entry;
import static smallibs.suitcase.cases.utils.Maps.Map;

public class MapMatcherTest {
    @Test
    public void shouldMatchTypedObject() throws MatchingException {
        final Matcher<Map<String, Integer>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Entry("hello", 42)).then.value(true);
        matcher.caseOf(_).then.value(false);

        final HashMap<String, Integer> map = new HashMap<>();
        map.put("hello", 42);

        TestCase.assertTrue(matcher.match(map));
    }

    @Test
    public void shouldMatchTypedObjects() throws MatchingException {
        final Matcher<Map<String, Integer>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Map(Entry("hello", 42), Entry("world", 19))).then.value(true);
        matcher.caseOf(_).then.value(false);

        final HashMap<String, Integer> map = new HashMap<>();
        map.put("hello", 42);
        map.put("world", 19);

        TestCase.assertTrue(matcher.match(map));
    }
}
