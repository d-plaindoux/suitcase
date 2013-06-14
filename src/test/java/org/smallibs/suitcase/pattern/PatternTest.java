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
package org.smallibs.suitcase.pattern;

import junit.framework.TestCase;
import org.junit.Test;

public class PatternTest {

    // Null

    @Test
    public void shouldMatchNullValueWithNull() {
        TestCase.assertFalse(Cases.nil().unapply(null).isNone());
    }

    @Test
    public void shouldNotMatchNotNullValueWithNull() {
        TestCase.assertTrue(Cases.nil().unapply(42).isNone());
    }

    // Constant

    @Test
    public void shouldMatchConstantValueWithConstant() {
        TestCase.assertFalse(Cases.constant(42).unapply(42).isNone());
    }

    @Test
    public void shouldNotMatchConstantValueWithDifferentConstant() {
        TestCase.assertTrue(Cases.constant(42).unapply(19).isNone());
    }

    @Test
    public void shouldNotMatchNullValueWithConstant() {
        TestCase.assertTrue(Cases.constant(42).unapply(null).isNone());
    }

    // TypeOf

    @Test
    public void shouldMatchValueValueWithTypeOf() {
        TestCase.assertFalse(Cases.typeOf(Integer.class).unapply(19).isNone());
    }

    @Test
    public void shouldNotMatchValueValueWithWrongTypeOf() {
        TestCase.assertTrue(Cases.typeOf(String.class).unapply(19).isNone());
    }

    @Test
    public void shouldNotMatchNullValueWithTypeOf() {
        TestCase.assertTrue(Cases.typeOf(String.class).unapply(null).isNone());
    }

    @Test
    public void shouldMatchSubTypeValueWithTypeOf() {
        TestCase.assertFalse(Cases.typeOf(Object.class).unapply("toto").isNone());
    }

    // Any

    @Test
    public void shouldMatchNullValueWithAny() {
        TestCase.assertFalse(Cases.any().unapply(null).isNone());
    }

    @Test
    public void shouldMatchNotNullValueWithAny() {
        TestCase.assertFalse(Cases.any().unapply(42).isNone());
    }
}

