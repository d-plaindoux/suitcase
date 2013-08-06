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
        final Boolean match = JSon.validate(JSon.stream("{}"));
        TestCase.assertTrue(match);
    }

    @Test
    public void shouldMatchEmptyArray() {
        TestCase.assertTrue(JSon.validate(JSon.stream("[]")));
    }

    @Test
    public void shouldMatchArrayWithAnEmptyObject() {
        TestCase.assertTrue(JSon.validate(JSon.stream("[{}]")));
    }

    @Test
    public void shouldMatchArrayWithNull() {
        TestCase.assertTrue(JSon.validate(JSon.stream("[null]")));
    }

    @Test
    public void shouldMatchArrayWithTrue() {
        TestCase.assertTrue(JSon.validate(JSon.stream("[true]")));
    }

    @Test
    public void shouldMatchArrayWithFalse() {
        TestCase.assertTrue(JSon.validate(JSon.stream("[false]")));
    }

    @Test
    public void shouldMatchArrayWithInt() {
        TestCase.assertTrue(JSon.validate(JSon.stream("[123]")));
    }

    @Test
    public void shouldMatchArrayWithString() {
        TestCase.assertTrue(JSon.validate(JSon.stream("['toto']")));
    }

    @Test
    public void shouldMatchArrayWithQuotedString() {
        TestCase.assertTrue(JSon.validate(JSon.stream("['toto']")));
    }

    @Test
    public void shouldMatchObjectWithBooleanNamedAttribute() {
        TestCase.assertTrue(JSon.validate(JSon.stream("{'toto':true}")));
    }

    @Test
    public void shouldMatchObjectWithTwoBooleanNamedAttribute2() {
        TestCase.assertTrue(JSon.validate(JSon.stream("{'toto':true,'titi':null}")));
    }

    @Test
    public void shouldMatchObjectWithEncapsulatedObject() {
        TestCase.assertTrue(JSon.validate(JSon.stream("{ 'toto' : { 'titi' : null } }")));
    }

    @Test
    public void shouldMatchJSONObject() {
        final String jsonValue = "{\n" +
                "  'users':[\n" +
                "    {\n" +
                "      'name': 'Bob',\n" +
                "      'age': 31.0,\n" +
                "      'french': true,\n" +
                "      'email': 'bob@gmail.com'\n" +
                "    },\n" +
                "    {\n" +
                "      'name': 'Kiki',\n" +
                "      'age':  25.0,\n" +
                "      'french': false,\n" +
                "      'email': null\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        TestCase.assertTrue(JSon.validate(JSon.stream(jsonValue)));
    }
}
