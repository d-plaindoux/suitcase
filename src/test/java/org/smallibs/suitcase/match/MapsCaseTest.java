/*
 *
 *  * Copyright (C)2013 D. Plaindoux.
 *  *
 *  * This program is free software; you can redistribute it and/or modify it
 *  * under the terms of the GNU Lesser General Public License as published
 *  * by the Free Software Foundation; either version 2, or (at your option) any
 *  * later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with this program; see the file COPYING.  If not, write to
 *  * the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.lang.Strings;

import java.util.HashMap;
import java.util.Map;

import static org.smallibs.suitcase.cases.core.Cases.Any;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.cases.lang.Maps.Entry;
import static org.smallibs.suitcase.utils.Functions.function;

public class MapsCaseTest {

    @Test
    public void shouldRetrieveAndEntry() throws Exception {
        final Matcher<Map<Integer, String>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Entry(1, "a")).then(true);

        final HashMap<Integer, String> map = new HashMap<Integer, String>() {{
            this.put(1, "a");
        }};

        TestCase.assertTrue(matcher.match(map));
    }

    @Test
    public void shouldRetrieveAndEntryUsingRegexp() throws Exception {
        final Matcher<Map<Integer, String>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Entry(1, Strings.Regex("a+"))).then(true);

        final HashMap<Integer, String> map = new HashMap<Integer, String>() {{
            this.put(1, "a");
        }};

        TestCase.assertTrue(matcher.match(map));
    }

    @Test
    public void shouldRetrieveAndCaptureEntryUsingRegexp() throws Exception {
        final Matcher<Map<Integer, String>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Entry(1, Var())).when(s -> s.equals("a")).then(true);

        final HashMap<Integer, String> map = new HashMap<Integer, String>() {{
            this.put(1, "a");
        }};

        TestCase.assertTrue(matcher.match(map));
    }

    @Test
    public void shouldRetrieveAndCaptureEntryUsingKeyRegexp() throws Exception {
        final Matcher<Map<String, String>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Entry(Strings.Regex("1|2"), Var())).when(s -> s.equals("a")).then(true);

        final HashMap<String, String> map = new HashMap<String, String>() {{
            this.put("1", "a");
        }};

        TestCase.assertTrue(matcher.match(map));
    }

    @Test
    public void shouldNotRetrieveEntryUsingRegexp() throws Exception {
        final Matcher<Map<Integer, String>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Entry(1, Var("b"))).then(true);
        matcher.caseOf(Any()).then(false);

        final HashMap<Integer, String> map = new HashMap<Integer, String>() {{
            this.put(1, "a");
        }};

        TestCase.assertFalse(matcher.match(map));
    }

    @Test
    public void shouldMatchNonEmptyMap() throws Exception {
        final Matcher<Map<Integer, String>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Entry(Any(), Any())).then(true);
        matcher.caseOf(Any()).then(false);

        final HashMap<Integer, String> map = new HashMap<Integer, String>() {{
            this.put(1, "a");
        }};

        TestCase.assertTrue(matcher.match(map));
    }

    @Test
    public void shouldCaptureAnEntry() throws Exception {
        final Matcher<Map<Integer, String>, Boolean> matcher = Matcher.create();

        matcher.caseOf(Entry(Var(), Var())).then(function((o1, o2) -> o1 == 1 && o2.equals("a")));
        matcher.caseOf(Any()).then(false);

        final HashMap<Integer, String> map = new HashMap<Integer, String>() {{
            this.put(1, "a");
        }};

        TestCase.assertTrue(matcher.match(map));
    }
}