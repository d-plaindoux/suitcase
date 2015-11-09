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

import org.smallibs.suitcase.utils.Pair;

import java.io.IOException;
import java.util.Optional;

public class TokenStream {

    private final AnalysedCharSequence sequence;
    private final Lexer lexer;

    protected TokenStream(Lexer lexer, CharSequence sequence) {
        this.lexer = lexer;
        this.sequence = new AnalysedCharSequence(sequence);
    }

    public static TokenStream stream(Lexer lexer, CharSequence sequence) {
        return new TokenStream(lexer, sequence);
    }

    public Token<?> nextToken() throws IOException, UnexpectedCharException {
        if (sequence.length() == 0) {
            throw new IOException();
        }

        performSkip();

        for (Tokenizer<?> token : lexer.getTokenizers()) {
            final Optional<? extends Token<?>> recognize = token.recognize(sequence);
            if (recognize.isPresent()) {
                this.sequence.commit(recognize.get().length());
                return recognize.get();
            }
        }

        throw new UnexpectedCharException(sequence.index, sequence.charAt(0));
    }

    public boolean isEmpty() {
        performSkip();
        return sequence.length() == 0;
    }

    private void performSkip() {
        boolean skipped;
        do {
            skipped = false;
            for (Tokenizer<?> token : lexer.getSkipped()) {
                final Optional<? extends Token<?>> recognize = token.recognize(sequence);
                if (recognize.isPresent()) {
                    this.sequence.commit(recognize.get().length());
                    skipped = true;
                    break;
                }
            }
        } while (skipped);
    }

    public Pair<TokenStream, Lexer> setLexer(Lexer lexer) {
        return new Pair<>(new TokenStream(lexer, sequence), this.lexer);
    }

    //
    // Private class dedicated to char sequence analysis
    //

    static final class AnalysedCharSequence implements CharSequence {

        private final CharSequence sequence;
        private int index = 0;

        AnalysedCharSequence(CharSequence sequence) {
            this.sequence = sequence;
            this.index = 0;
        }

        private AnalysedCharSequence(AnalysedCharSequence sequence) {
            this.sequence = sequence.sequence;
            this.index = sequence.index;
        }

        void commit(int offset) {
            this.index += offset;
        }

        @Override
        public int length() {
            return sequence.length() - index;
        }

        @Override
        public char charAt(int index) {
            return sequence.charAt(this.index + index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return sequence.subSequence(this.index + start, this.index + end);
        }

        public AnalysedCharSequence copy() {
            return new AnalysedCharSequence(this);
        }
    }
}
