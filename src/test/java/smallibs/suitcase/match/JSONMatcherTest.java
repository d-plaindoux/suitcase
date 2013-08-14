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

import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import smallibs.suitcase.cases.json.GSonBuilder;
import smallibs.suitcase.cases.json.JSon;
import smallibs.suitcase.cases.json.POJOBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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
        TestCase.assertTrue(JSon.validate(JSon.stream("[\"toto\"]")));
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
        TestCase.assertTrue(JSon.validate(JSon.stream(" { 'toto' : { 'titi' : null } } ")));
    }

    @Test
    public void shouldMatchJSONObject() {
        final String jsonValue = getJsonSample();

        TestCase.assertTrue(JSon.validate(JSon.stream(jsonValue)));
    }

    @Test
    public void shouldBuildJSONObject() {
        final String jsonValue = getJsonSample();

        final Object json = JSon.withHandler(new POJOBuilder()).match(JSon.stream(jsonValue));

        TestCase.assertNotNull(json);
        TestCase.assertEquals(HashMap.class, json.getClass());
    }

    @Test
    public void shouldBuildGSONObject() {
        final String jsonValue = getJsonSample();

        final Object json = JSon.withHandler(new GSonBuilder()).match(JSon.stream(jsonValue));

        TestCase.assertNotNull(json);
        TestCase.assertEquals(JsonObject.class, json.getClass());
    }

    @Test
    public void shouldBuildBigGSONObject() throws IOException {
        final String jsonValue;

        try (InputStream inputStream = JSONMatcherTest.class.getResource("/sample.json").openStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            jsonValue = outputStream.toString();
        }

        final long t0 = System.currentTimeMillis();
        final Object json = JSon.withHandler(new GSonBuilder()).match(JSon.stream(jsonValue));
        // System.out.println("<INFO> Parse JSON in " + (System.currentTimeMillis() - t0) + "ms");

        TestCase.assertNotNull(json);
        TestCase.assertEquals(JsonObject.class, json.getClass());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private String getJsonSample() {
        return "{\n" +
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
    }
}
