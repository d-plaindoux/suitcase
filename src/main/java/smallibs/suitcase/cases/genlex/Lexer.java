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

package smallibs.suitcase.cases.genlex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static smallibs.suitcase.cases.genlex.TokenRecognizer.Ident;

public class Lexer {

    private final List<TokenRecognizer> recognizers;
    private final List<TokenRecognizer> skipped;

    public Lexer(TokenRecognizer... recognizers) {
        this.recognizers = new ArrayList<>();
        this.skipped = new ArrayList<>();
        this.recognizers(recognizers);
    }

    public Lexer skip(String... skipped) {
        for (String value : skipped) {
            this.skipped.add(TokenRecognizer.Skip(value));
        }
        return this;
    }

    public Lexer recognizers(TokenRecognizer... recognizers) {
        this.recognizers.addAll(Arrays.asList(recognizers));
        return this;
    }

    public TokenStream parse(CharSequence sequence) {
        return new TokenStream(this, sequence);
    }

    List<TokenRecognizer> getRecognizers() {
        return recognizers;
    }

    public List<TokenRecognizer> getSkipped() {
        return skipped;
    }

    public Lexer keywords(String... keywords) {
        for (String keyword : keywords) {
            this.recognizers.add(TokenRecognizer.Keyword(keyword));
        }
        return this;
    }
}
