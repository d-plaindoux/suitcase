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
import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.cases.genlex.UnexpectedCharException;

import java.io.IOException;

import static smallibs.suitcase.cases.genlex.TokenRecognizer.Hexa;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Ident;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Int;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.String;

public class GenLexStreamTest {

    @Test
    public void shouldHaveLACCToken() throws Exception {
        final TokenStream stream = new Lexer().keywords("{", "}").parse("{");
        TestCase.assertEquals("{", stream.nextToken().value());
    }

    @Test
    public void shouldHaveRACCToken() throws Exception {
        final TokenStream stream = new Lexer().keywords("{", "}").parse("}");
        TestCase.assertEquals("}", stream.nextToken().value());
    }

    @Test(expected = IOException.class)
    public void shouldHaveIOExceptionWhenEmptySequence() throws Exception {
        final TokenStream stream = new Lexer().keywords("{", "}").parse("");
        stream.nextToken();
    }

    @Test(expected = UnexpectedCharException.class)
    public void shouldHaveUnexpectedCharException() throws Exception {
        final TokenStream stream = new Lexer().keywords("{", "}").parse("|");
        stream.nextToken();
    }

    @Test
    public void shouldHaveTwoTokens() throws Exception {
        final TokenStream stream = new Lexer().keywords("{", "}").parse("{}");
        TestCase.assertEquals("{", stream.nextToken().value());
        TestCase.assertEquals("}", stream.nextToken().value());
    }

    @Test
    public void shouldHaveASetOfTokensBasedOnBrainF_ck() throws Exception {
        final String sequence = ",[.[-],]";
        final TokenStream stream = new Lexer().keywords("[", "]", "+", "-", ".", ",").parse(sequence);
        int i = 0;
        while (!stream.isEmpty()) {
            TestCase.assertEquals(String.valueOf(sequence.charAt(i)), stream.nextToken().value());
            i += 1;
        }
    }

    @Test
    public void shouldHaveTwoTokensAgain() throws Exception {
        final TokenStream stream = new Lexer().keywords("++", "-").parse("-++-");
        TestCase.assertEquals("-", stream.nextToken().value());
        TestCase.assertEquals("++", stream.nextToken().value());
        TestCase.assertEquals("-", stream.nextToken().value());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Ident
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldHaveOneIdentifier() throws Exception {
        final TokenStream stream = new Lexer(Ident("[a-z]+")).parse("hello world");
        TestCase.assertEquals("hello", stream.nextToken().value());
    }

    @Test(expected = UnexpectedCharException.class)
    public void shouldNotHaveOneIdentifier() throws Exception {
        final TokenStream stream = new Lexer(Ident("[a-z]+")).parse("123 hello");
        stream.nextToken().value();
    }

    @Test
    public void shouldHaveOneString() throws Exception {
        final TokenStream stream = new Lexer(String()).parse("\"World!\"");
        TestCase.assertEquals("World!", stream.nextToken().value());
    }

    @Test
    public void shouldHaveOneInteger() throws Exception {
        final TokenStream stream = new Lexer(Int()).parse("1942");
        TestCase.assertEquals(1942, stream.nextToken().value());
    }

    @Test
    public void shouldHaveOneHexa() throws Exception {
        final TokenStream stream = new Lexer(Hexa()).parse("0x1942");
        TestCase.assertEquals(0x1942, stream.nextToken().value());
    }

    @Test
    public void shouldHaveOneHexaStringAndInt() throws Exception {
        final TokenStream stream = new Lexer(Hexa(), Int(), Ident("[a-zA-Z]+")).parse("1942Hello0x12");
        TestCase.assertEquals(1942, stream.nextToken().value());
        TestCase.assertEquals("Hello", stream.nextToken().value());
        TestCase.assertEquals(0x12, stream.nextToken().value());
    }

}

