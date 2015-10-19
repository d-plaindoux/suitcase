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

import java.util.Optional;

import java.io.IOException;

public abstract class TokenStream {

    private final AnalysedCharSequence sequence;

    // -----------------------------------------------------------------------------------------------------------------
    private Lexer lexer;
    protected TokenStream(Lexer lexer, CharSequence sequence) {
        this.lexer = lexer;
        this.sequence = new AnalysedCharSequence(sequence);
    }

    protected TokenStream(TokenStream tokenStream) {
        this.lexer = tokenStream.lexer;
        this.sequence = tokenStream.sequence.clone();
    }

    public static TokenStream stream(Lexer lexer, CharSequence sequence) {
        return new InitialTokenStream(lexer, sequence);
    }

    public void commit(TokenStream stream) {
        this.sequence.commit(stream.sequence.index - this.sequence.index);
    }

    public Token nextToken() throws IOException, UnexpectedCharException {
        if (sequence.length() == 0) {
            throw new IOException();
        }

        performSkip();

        for (Tokenizer token : lexer.getTokenizers()) {
            final Optional<Token<?>> recognize = token.recognize(sequence);
            if (recognize.isPresent()) {
                this.sequence.commit(recognize.get().length());
                return recognize.get();
            }
        }

        throw new UnexpectedCharException(sequence.index, sequence.charAt(0));
    }

    public boolean isEmpty() {

        performSkip(); // TODO check if this is the right place

        return sequence.length() == 0;
    }

    public TokenStream secondary() {
        return new SecondaryTokenStream(this);
    }

    abstract public boolean isInitial();

    private void performSkip() {
        boolean skipped;
        do {
            skipped = false;
            for (Tokenizer token : lexer.getSkipped()) {
                final Optional<Token<?>> recognize = token.recognize(sequence);
                if (recognize.isPresent()) {
                    this.sequence.commit(recognize.get().length());
                    skipped = true;
                    break;
                }
            }
        } while (skipped);
    }

    public Lexer setLexer(Lexer lexer) {
        Lexer replacedLexer = this.lexer;
        this.lexer = lexer;
        return replacedLexer;
    }

    // -----------------------------------------------------------------------------------------------------------------

    static class SecondaryTokenStream extends TokenStream {

        SecondaryTokenStream(TokenStream tokenStream) {
            super(tokenStream);
        }

        @Override
        public boolean isInitial() {
            return false;
        }
    }

    static class InitialTokenStream extends TokenStream {

        InitialTokenStream(Lexer lexer, CharSequence sequence) {
            super(lexer, sequence);
        }

        @Override
        public boolean isInitial() {
            return true;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    static final class AnalysedCharSequence implements CharSequence {

        private final CharSequence sequence;
        private int index = 0;

        AnalysedCharSequence(CharSequence sequence) {
            this.sequence = sequence;
            this.index = 0;
        }

        AnalysedCharSequence commit(int offset) {
            this.index += offset;
            return this;
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

        public AnalysedCharSequence clone() {
            return new AnalysedCharSequence(this.sequence).commit(this.index);
        }
    }
}