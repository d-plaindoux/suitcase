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
import smallibs.suitcase.cases.genlex.JavaLexer;
import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.cases.genlex.UnexpectedCharException;

import java.io.IOException;

import static smallibs.suitcase.cases.genlex.TokenRecognizer.Float;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Hexa;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Ident;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Int;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Keyword;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.QuotedString;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.String;

public class GenLexStreamTokenTest {

    @Test
    public void shouldHaveLACCToken() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Keyword("{"), Keyword("}")).parse("{");
        TestCase.assertEquals("{", stream.nextToken().value());
    }

    @Test
    public void shouldHaveRACCToken() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Keyword("{"), Keyword("}")).parse("}");
        TestCase.assertEquals("}", stream.nextToken().value());
    }

    @Test(expected = IOException.class)
    public void shouldHaveIOExceptionWhenEmptySequence() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Keyword("{"), Keyword("}")).parse("");
        stream.nextToken();
    }

    @Test(expected = UnexpectedCharException.class)
    public void shouldHaveUnexpectedCharException() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Keyword("{"), Keyword("}")).parse("|");
        stream.nextToken();
    }

    @Test
    public void shouldHaveTwoTokens() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Keyword("{"),Keyword("}")).parse("{}");
        TestCase.assertEquals("{", stream.nextToken().value());
        TestCase.assertEquals("}", stream.nextToken().value());
    }

    @Test
    public void shouldHaveASetOfTokensBasedOnBrainF_ck() throws Exception {
        final String sequence = ",[.[-],]";
        final TokenStream stream = new Lexer().recognizers(Keyword("["), Keyword("]"), Keyword("+"), Keyword("-"), Keyword("."), Keyword(",")).parse(sequence);
        int i = 0;
        while (!stream.isEmpty()) {
            TestCase.assertEquals(String.valueOf(sequence.charAt(i)), stream.nextToken().value());
            i += 1;
        }
    }

    @Test
    public void shouldHaveThreeTokens() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Keyword("++"), Keyword("-")).parse("-++-");
        TestCase.assertEquals("-", stream.nextToken().value());
        TestCase.assertEquals("++", stream.nextToken().value());
        TestCase.assertEquals("-", stream.nextToken().value());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Ident
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldHaveOneIdentifier() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Ident("[a-z]+")).parse("hello world");
        TestCase.assertEquals("hello", stream.nextToken().value());
    }

    @Test(expected = UnexpectedCharException.class)
    public void shouldNotHaveOneIdentifier() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Ident("[a-z]+")).parse("123 hello");
        stream.nextToken().value();
    }

    @Test
    public void shouldHaveOneString() throws Exception {
        final TokenStream stream = new Lexer().recognizers(String()).parse("\"World!\"");
        TestCase.assertEquals("World!", stream.nextToken().value());
    }

    @Test
    public void shouldHaveOneQuotedString() throws Exception {
        final TokenStream stream = new Lexer().recognizers(QuotedString()).parse("'World!'");
        TestCase.assertEquals("World!", stream.nextToken().value());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldHaveOneInteger() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Int()).parse("1942");
        TestCase.assertEquals(1942, stream.nextToken().value());
    }

    @Test
    public void shouldHaveOneInteger2() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Int()).parse("-1942");
        TestCase.assertEquals(-1942, stream.nextToken().value());
    }

    @Test
    public void shouldHaveOneHexa() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Hexa()).parse("0x1942");
        TestCase.assertEquals(0x1942, stream.nextToken().value());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Float
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldHaveOneFloat() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Float()).parse("1942.43");
        TestCase.assertEquals(1942.43f, stream.nextToken().value());
    }

    @Test
    public void shouldHaveOneFloat2() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Float()).parse("42e2");
        TestCase.assertEquals(42e2f, stream.nextToken().value());
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Error case when pattern matches empty
    // -----------------------------------------------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void shouldHaveExceptionWhenPatternMatchesEmpty() throws Exception {
        new Lexer().skip("\\s*");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Mixin' Int, Hexa and Ident
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldHaveOneHexaStringAndInt() throws Exception {
        final TokenStream stream = new Lexer().recognizers(Hexa(), Int(), Ident("[a-zA-Z]+")).parse("1942Hello0x12");
        TestCase.assertEquals(1942, stream.nextToken().value());
        TestCase.assertEquals("Hello", stream.nextToken().value());
        TestCase.assertEquals(0x12, stream.nextToken().value());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Mixin' Int, Hexa and Ident and Skipped spaces
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldHaveOneHexaStringAndIntWithSkippedSpaces() throws Exception {
        final TokenStream stream = new JavaLexer().parse("1942 Hello 0x12");
        TestCase.assertEquals(1942, stream.nextToken().value());
        TestCase.assertEquals("Hello", stream.nextToken().value());
        TestCase.assertEquals(0x12, stream.nextToken().value());
    }

}

