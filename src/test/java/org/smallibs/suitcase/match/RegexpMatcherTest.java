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

import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.cases.core.Cases.Any;
import static org.smallibs.suitcase.cases.lang.Strings.Regex;

public class RegexpMatcherTest {

    @Test
    public void shouldMatchStringUsingRegex() throws Exception {
        final Matcher<String, Boolean> matcher = Matcher.create();

        matcher.caseOf(Regex("Hello")).then(true);
        matcher.caseOf(Any()).then(false);

        TestCase.assertTrue(matcher.match("Hello"));
    }

    @Test
    public void shouldNotMatchStringUsingRegex() throws Exception {
        final Matcher<String, Boolean> matcher = Matcher.create();

        matcher.caseOf(Regex("Hello")).then(true);
        matcher.caseOf(Any()).then(false);

        TestCase.assertFalse(matcher.match("Hello, World"));
    }

    @Test
    public void shouldMatchSubStringUsingRegex() throws Exception {
        final Matcher<String, Boolean> matcher = Matcher.create();

        matcher.caseOf(Var(Regex("Hello, (.*)"))).then(strings -> strings.get(1).equals("World"));
        matcher.caseOf(Any()).then(false);

        TestCase.assertTrue(matcher.match("Hello, World"));
    }

    @Test
    public void shouldNotMatchSubStringUsingRegex() throws Exception {
        final Matcher<String, Boolean> matcher = Matcher.create();

        matcher.caseOf(Regex("Hello, .*!")).then(true);
        matcher.caseOf(Any()).then(false);

        TestCase.assertFalse(matcher.match("Hello, World"));
    }
}
