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
import org.smallibs.suitcase.pattern.utils.Lists;
import org.smallibs.suitcase.pattern.utils.Pairs;
import org.smallibs.suitcase.utils.Pair;

import java.util.Arrays;

import static org.smallibs.suitcase.pattern.Cases._;
import static org.smallibs.suitcase.pattern.Cases.var;
import static org.smallibs.suitcase.pattern.peano.Peano.Succ;
import static org.smallibs.suitcase.pattern.peano.Peano.Zero;

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

    // List

    @Test
    public void shouldNilMatchEmptyList() {
        TestCase.assertFalse(Lists.Empty().unapply(Arrays.asList()).isNone());
    }

    @Test
    public void shouldNilNotlMatchNonList() {
        TestCase.assertTrue(Lists.<Integer>Empty().unapply(Arrays.asList(1)).isNone());
    }

    @Test
    public void shouldConsNotlMatchEmptyList() {
        TestCase.assertTrue(Lists.Cons(_, _).unapply(Arrays.asList()).isNone());
    }

    @Test
    public void shouldConsMatchNonEmptyList() {
        TestCase.assertFalse(Lists.<Integer>Cons(_, _).unapply(Arrays.asList(1)).isNone());
    }

    @Test
    public void shouldConsMatchListSize2() {
        TestCase.assertFalse(Lists.<Integer>Cons(_, Lists.Cons(_, _)).unapply(Arrays.asList(1, 2)).isNone());
    }

    @Test
    public void shouldConsMatchListSize2Exactly() {
        TestCase.assertFalse(var.of(Lists.<Integer>Cons(_, Lists.Cons(_, Lists.Empty()))).unapply(Arrays.asList(1, 2)).isNone());
    }

    @Test
    public void shouldMatchTheListExactly() {
        TestCase.assertFalse(Lists.<Integer>Cons(1, Lists.Cons(2, Lists.Empty())).unapply(Arrays.asList(1, 2)).isNone());
    }

    // $Pair

    @Test
    public void shouldMatchPair() {
        TestCase.assertFalse(Pairs.APair(_, _).unapply(new Pair<Object, Object>(1, 2)).isNone());
    }

    @Test
    public void shouldMatchPairAndValues() {
        TestCase.assertFalse(Pairs.APair(1, 2).unapply(new Pair<Object, Object>(1, 2)).isNone());
    }

    // Peano

    @Test
    public void shouldMatchZero() {
        TestCase.assertFalse(Zero().unapply(0).isNone());
    }

    @Test
    public void shouldNotMatchZero() {
        TestCase.assertTrue(Zero().unapply(1).isNone());
    }

    @Test
    public void shouldMatchNonZero() {
        TestCase.assertFalse(Succ(_).unapply(1).isNone());
    }

    @Test
    public void shouldNotMatchNonZero() {
        TestCase.assertTrue(var.of(Succ(_)).unapply(0).isNone());
    }
}

