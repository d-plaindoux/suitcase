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

import static org.smallibs.suitcase.cases.core.Cases.var;

public class ConditionalMatcherTest {

    @Test
    public void shouldMatchWhenNull() {
        final Matcher<Integer, Boolean> matcher = Matcher.create();

        matcher.caseOf(var).when(var1 -> var1 == null).then(true);
        matcher.caseOf(var).then(false);

        TestCase.assertTrue(matcher.match(null));
    }

    @Test
    public void shouldNotMatchWhenNotNull() {
        final Matcher<Object, Boolean> matcher = Matcher.create();

        matcher.caseOf(var).when(var1 -> var1 == null).then(true);
        matcher.caseOf(var).then(false);

        TestCase.assertFalse(matcher.match("Hello"));
    }

}
