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
import org.smallibs.suitcase.utils.Function;

import static org.smallibs.suitcase.cases.core.Cases.__;
import static org.smallibs.suitcase.cases.core.Cases.var;
import static org.smallibs.suitcase.cases.lang.Beans.Att;
import static org.smallibs.suitcase.cases.lang.Beans.Obj;

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

        matcher.caseOf(Obj(Att("a", __))).then(true);
        matcher.caseOf(__).then(false);

        TestCase.assertTrue(matcher.match(new A(42)));
    }

    @Test
    public void shouldNotMatchABeanWithAGivenAttName() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Obj(Att("b", __))).then(false);
        matcher.caseOf(__).then(true);

        TestCase.assertTrue(matcher.match(new A(42)));
    }

    @Test
    public void shouldNotMatchABeanWithAGivenAttValue() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Obj(Att("a", 19))).then(false);
        matcher.caseOf(__).then(true);

        TestCase.assertTrue(matcher.match(new A(42)));
    }

    @Test
    public void shouldMatchABeanWithAGivenAttValue() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Obj(Att("a", 42))).then(true);
        matcher.caseOf(__).then(false);

        TestCase.assertTrue(matcher.match(new A(42)));
    }

    @Test
    public void shouldMatchABeanWithAGivenMethodValue() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Obj(Att("ceci", 42))).then(true);
        matcher.caseOf(__).then(false);

        TestCase.assertTrue(matcher.match(new A(21)));
    }

    @Test
    public void shouldMatchABeanWithAGivenValue() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Obj(Att(__, 42))).then(true);
        matcher.caseOf(__).then(false);

        TestCase.assertTrue(matcher.match(new A(21)));
    }

    @Test
    public void shouldMatchABeanWithGivenAttributes() {
        final Matcher<A, Boolean> matcher = Matcher.create();

        matcher.caseOf(Obj(Att("a", 21), Att("ceci", 42))).then(true);
        matcher.caseOf(__).then(false);

        TestCase.assertTrue(matcher.match(new A(21)));
    }

    @Test
    public void shouldMatchAStringContent() {
        final Matcher<Object, Integer> matcher = Matcher.create();

        matcher.caseOf(Obj(Att("bytes", var))).then(new Function<byte[], Integer>() {
            public Integer apply(byte[] bytes) {
                return bytes.length;
            }
        });
        matcher.caseOf(__).then(0);

        TestCase.assertEquals(13, matcher.match("Hello, World!").intValue());
    }
}
