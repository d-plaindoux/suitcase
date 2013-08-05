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
import smallibs.suitcase.cases.json.JSon;

public class JSONMatcherTest {

    @Test
    public void shouldMatchEmptyObject() {
        TestCase.assertTrue(JSon.match(JSon.stream("{}")));
    }

    @Test
    public void shouldMatchEmptyArray() {
        TestCase.assertTrue(JSon.match(JSon.stream("[]")));
    }

    @Test
    public void shouldMatchArrayWithAnEmptyObject() {
        TestCase.assertTrue(JSon.match(JSon.stream("[{}]")));
    }

    @Test
    public void shouldMatchArrayWithNull() {
        TestCase.assertTrue(JSon.match(JSon.stream("[null]")));
    }

    @Test
    public void shouldMatchArrayWithTrue() {
        TestCase.assertTrue(JSon.match(JSon.stream("[true]")));
    }

    @Test
    public void shouldMatchArrayWithFalse() {
        TestCase.assertTrue(JSon.match(JSon.stream("[false]")));
    }

    @Test
    public void shouldMatchArrayWithInt() {
        TestCase.assertTrue(JSon.match(JSon.stream("[123]")));
    }

    @Test
    public void shouldMatchArrayWithString() {
        TestCase.assertTrue(JSon.match(JSon.stream("[\"toto\"]")));
    }

    @Test
    public void shouldMatchArrayWithQuotedString() {
        TestCase.assertTrue(JSon.match(JSon.stream("['toto']")));
    }

    @Test
    public void shouldMatchObjectWithBooleanNamedAttribute() {
        TestCase.assertTrue(JSon.match(JSon.stream("{'toto':true}")));
    }

    @Test
    public void shouldMatchObjectWithTwoBooleanNamedAttribute2() {
        TestCase.assertTrue(JSon.match(JSon.stream("{'toto':true,'titi':null}")));
    }

    @Test
    public void shouldMatchObjectWithEncapsulatedObject() {
        TestCase.assertTrue(JSon.match(JSon.stream("{ 'toto' : { 'titi' : null } }")));
    }
}
