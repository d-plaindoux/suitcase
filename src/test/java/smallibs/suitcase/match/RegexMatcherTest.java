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

import java.util.List;

import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.core.Cases.var;
import static smallibs.suitcase.cases.lang.Strings.Regex;

public class RegexMatcherTest {

    @Test
    public void shouldMatchStringUsingRegex() throws Exception {
        final Matcher<String, Boolean> matcher = Matcher.create();

        matcher.caseOf(Regex("Hello")).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match("Hello"));
    }

    @Test
    public void shouldNotMatchStringUsingRegex() throws Exception {
        final Matcher<String, Boolean> matcher = Matcher.create();

        matcher.caseOf(Regex("Hello")).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertFalse(matcher.match("Hello, World"));
    }

    @Test
    public void shouldMatchSubStringUsingRegex() throws Exception {
        final Matcher<String, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(Regex("Hello, (.*)"))).then.function(new Function<List<String>, Boolean>() {
            public Boolean apply(List<String> strings) {
                return strings.get(1).equals("World");
            }
        });
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match("Hello, World"));
    }

    @Test
    public void shouldNotMatchSubStringUsingRegex() throws Exception {
        final Matcher<String, Boolean> matcher = Matcher.create();

        matcher.caseOf(Regex("Hello, .*!")).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertFalse(matcher.match("Hello, World"));
    }
}
