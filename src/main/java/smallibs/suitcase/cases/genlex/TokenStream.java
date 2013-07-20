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

import smallibs.suitcase.utils.Option;

import java.io.IOException;

public class TokenStream {

    private final class AnalysedCharSequence implements CharSequence {

        private final CharSequence sequence;
        private int index = 0;

        private AnalysedCharSequence(CharSequence sequence) {
            this.sequence = sequence;
            this.index = 0;
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
    }

    private final Lexer lexer;
    private final AnalysedCharSequence sequence;

    public TokenStream(Lexer lexer, CharSequence sequence) {
        this.lexer = lexer;
        this.sequence = new AnalysedCharSequence(sequence);
    }

    public Token nextToken() throws IOException, UnexpectedCharException {
        if (sequence.length() == 0) {
            throw new IOException();
        }

        boolean skipped;
        do {
            skipped = false;
            for (TokenRecognizer token : lexer.getSkipped()) {
                final Option<Token<?>> recognize = token.recognize(sequence);
                if (!recognize.isNone()) {
                    this.sequence.commit(recognize.value().length());
                    skipped = true;
                    break;
                }
            }
        } while (skipped);

        for (TokenRecognizer token : lexer.getRecognizers()) {
            final Option<Token<?>> recognize = token.recognize(sequence);
            if (!recognize.isNone()) {
                this.sequence.commit(recognize.value().length());
                return recognize.value();
            }
        }

        throw new UnexpectedCharException(sequence.index, sequence.charAt(0));
    }

    public boolean isEmpty() {
        return sequence.length() == 0;
    }
}
