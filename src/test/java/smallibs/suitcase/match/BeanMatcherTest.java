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

import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.lang.Beans.Att;
import static smallibs.suitcase.cases.lang.Beans.Bean;

public class BeanMatcherTest {

    public static class A {
        public final int a;
        private final int ceci;

        public A(int a) {
            this.a = a;
            this.ceci = a * 2;
        }

        public int getCeci() {
            return ceci;
        }
    }

    @Test
    public void shouldMatchABeanWithAGivenAttName() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Bean(Att("a", _))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(new A(42)));
    }

    @Test
    public void shouldNotMatchABeanWithAGivenAttName() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Bean(Att("b", _))).then.value(false);
        matcher.caseOf(_).then.value(true);

        TestCase.assertTrue(matcher.match(new A(42)));
    }

    @Test
    public void shouldNotMatchABeanWithAGivenAttValue() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Bean(Att("a", 19))).then.value(false);
        matcher.caseOf(_).then.value(true);

        TestCase.assertTrue(matcher.match(new A(42)));
    }

    @Test
    public void shouldMatchABeanWithAGivenAttValue() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Bean(Att("a", 42))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(new A(42)));
    }

    @Test
    public void shouldMatchABeanWithAGivenMethodValue() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Bean(Att("ceci", 42))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(new A(21)));
    }
}
