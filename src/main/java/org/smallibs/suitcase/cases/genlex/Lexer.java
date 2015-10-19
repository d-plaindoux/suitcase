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

package org.smallibs.suitcase.cases.genlex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private final List<Tokenizer> recognizers;
    private final List<Tokenizer> skipped;

    public Lexer() {
        this.recognizers = new ArrayList<>();
        this.skipped = new ArrayList<>();
    }

    public Lexer skip(String... skipped) {
        for (String value : skipped) {
            this.skipped.add(Tokenizer.Skip(value));
        }
        return this;
    }

    public Lexer tokenizers(Tokenizer... recognizers) {
        this.recognizers.addAll(Arrays.asList(recognizers));
        return this;
    }

    public TokenStream parse(CharSequence sequence) {
        return TokenStream.stream(this, sequence);
    }

    public List<Tokenizer> getTokenizers() {
        return recognizers;
    }

    public List<Tokenizer> getSkipped() {
        return skipped;
    }
}
