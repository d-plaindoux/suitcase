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

package org.smallibs.suitcase.cases;

import org.junit.Test;
import org.smallibs.suitcase.cases.genlex.Lexer;
import org.smallibs.suitcase.cases.genlex.Token;
import org.smallibs.suitcase.cases.genlex.TokenStream;
import org.smallibs.suitcase.cases.genlex.Tokenizer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smallibs.suitcase.cases.genlex.Token.Float;
import static org.smallibs.suitcase.cases.genlex.Token.Generic;
import static org.smallibs.suitcase.cases.genlex.Token.Ident;
import static org.smallibs.suitcase.cases.genlex.Token.Int;
import static org.smallibs.suitcase.cases.genlex.Token.Keyword;
import static org.smallibs.suitcase.cases.genlex.Token.String;

public class LexerTest {

    @Test
    public void shouldHaveAnIdent() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("anIdent");

        final Token token = stream.nextToken();
        assertThat(token).isEqualToComparingFieldByField(Ident("anIdent"));
    }

    @Test
    public void shouldHaveAKeyword() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("new");

        final Token token = stream.nextToken();
        assertThat(token).isEqualToComparingFieldByField(Keyword("new"));
    }

    @Test
    public void shouldHaveAnotherKeyword() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("new");

        final Token token = stream.nextToken();
        assertThat(token).isEqualToComparingFieldByField(Keyword("new"));
    }

    @Test
    public void shouldHaveAnInteger() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("-12");

        final Token token = stream.nextToken();
        assertThat(token).isEqualToComparingFieldByField(Int(3, -12));
    }

    @Test
    public void shouldHaveAFloat() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("-12.3");

        final Token token = stream.nextToken();
        assertThat(token).isEqualToComparingFieldByField(Float(5, -12.3f));
    }

    @Test
    public void shouldHaveAString() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("\"-12\"");

        final Token token = stream.nextToken();
        assertThat(token).isEqualToComparingFieldByField(String(5, "-12"));
    }

    @Test
    public void shouldHaveAQuotedString() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("'-12'");

        final Token token = stream.nextToken();
        assertThat(token).isEqualToComparingFieldByField(String(5, "-12"));
    }

    @Test
    public void shouldHaveAGeneric() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("++");

        final Token token = stream.nextToken();
        assertThat(token).isEqualToComparingFieldByField(Generic("Operator", 2, "++"));
    }

    @Test
    public void shouldHaveAnIntegerAndAnIdent() throws Exception {
        final Lexer lexer = new ALexer();
        final TokenStream stream = lexer.parse("-12 World");

        final Token token1 = stream.nextToken();
        assertThat(token1).isEqualToComparingFieldByField(Int(3, -12));

        final Token token2 = stream.nextToken();
        assertThat(token2).isEqualToComparingFieldByField(Ident("World"));
    }

    //
    // Private behaviors
    //

    private static class ALexer extends Lexer {

        public static final Tokenizer IDENT =
                Tokenizer.Ident("([\\p{L}$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*");

        public ALexer() {
            super();
            this.tokenizers(Tokenizer.Generic("Operator", Tokenizer.Kwd("++")));
            // Keywords
            this.tokenizers(Tokenizer.Kwd("new"));
            // Hexadecimal Number
            this.tokenizers(Tokenizer.Hexa());
            // Float
            this.tokenizers(Tokenizer.Float());
            // Integer
            this.tokenizers(Tokenizer.Int());
            // String
            this.tokenizers(Tokenizer.QuotedString());
            // String
            this.tokenizers(Tokenizer.String());
            // Identifier
            this.tokenizers(IDENT);
            // Skip spaces
            this.skip("\\s+");
        }
    }
}

