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

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.Result;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tokenizer<T> implements Case.WithoutCapture<CharSequence, Token<T>> {

    public static Tokenizer<String> Kwd(String value) {
        return new KeywordRecognizer(value);
    }

    public static Tokenizer<String> Ident(String value) {
        return new IdentifierRecognizer(value);
    }

    public static Tokenizer<String> String() {
        return new StringRecognizer();
    }

    public static Tokenizer<String> QuotedString() {
        return new QuotedStringRecognizer();
    }

    public static Tokenizer<Integer> Int() {
        return new IntRecognizer();
    }

    public static Tokenizer<Float> Float() {
        return new FloatRecognizer();
    }

    public static Tokenizer<Integer> Hexa() {
        return new HexadecimalRecognizer();
    }

    public static Skip Skip(String value) {
        return new Skip(value);
    }

    public static <T> GenericRecognizer<T> Generic(String kind, Tokenizer<T> value) {
        return new GenericRecognizer<>(kind, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Public abstract methods
    // -----------------------------------------------------------------------------------------------------------------

    public abstract Optional<Token<T>> recognize(CharSequence sequence);

    @Override
    public Optional<Result.WithoutCapture<Token<T>>> unapply(CharSequence charSequence) {
        return this.recognize(charSequence).map(Result::success);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Private class
    // es
    // -----------------------------------------------------------------------------------------------------------------

    private static class KeywordRecognizer extends Tokenizer<String> {
        private final String value;

        private KeywordRecognizer(String value) {
            this.value = value;
        }

        @Override
        public Optional<Token<String>> recognize(CharSequence sequence) {
            if (value.length() <= sequence.length() && value.contentEquals(sequence.subSequence(0, value.length()))) {
                return Optional.of(Token.Keyword(value));
            } else {
                return Optional.empty();
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static abstract class PatternRecognizer<T> extends Tokenizer<T> {
        private final Pattern pattern;

        protected PatternRecognizer(String value) {
            this.pattern = Pattern.compile("^" + value);

            // Prevent empty pattern recognition
            if (this.pattern.matcher("").matches()) {
                throw new IllegalArgumentException();
            }
        }

        protected abstract Token<T> matched(String string);

        @Override
        public Optional<Token<T>> recognize(CharSequence sequence) {
            final Matcher matcher = pattern.matcher(sequence);
            if (matcher.find()) {
                final CharSequence recognized = sequence.subSequence(matcher.start(), matcher.end());
                return Optional.ofNullable(matched(recognized.toString()));
            } else {
                return Optional.empty();
            }
        }
    }

    public static class GenericRecognizer<T> extends Tokenizer<T> {
        private final String kind;
        private final Tokenizer<T> value;

        public GenericRecognizer(String kind, Tokenizer<T> value) {
            this.kind = kind;
            this.value = value;
        }

        @Override
        public Optional<Token<T>> recognize(CharSequence sequence) {
            final Optional<Token<T>> option = value.recognize(sequence);
            if (option.isPresent()) {
                final Token<T> value = option.get();
                final Token<T> generic = Token.Generic(kind, value.length(), value.value());
                return Optional.of(generic);
            } else {
                return Optional.empty();
            }
        }
    }

    private static class IdentifierRecognizer extends PatternRecognizer<String> {
        public IdentifierRecognizer(String value) {
            super(value);
        }

        @Override
        protected Token<String> matched(String string) {
            return Token.Ident(string);
        }
    }

    private static class StringRecognizer extends PatternRecognizer<String> {
        private StringRecognizer() {
            super("\"(\\\\.|[^\"])*\"");
        }

        @Override
        protected Token<String> matched(String string) {
            return Token.String(string.length(), string.substring(1, string.length() - 1));
        }
    }

    private static class QuotedStringRecognizer extends PatternRecognizer<String> {
        private QuotedStringRecognizer() {
            super("'(\\.|[^'])*'");
        }

        @Override
        protected Token<String> matched(String string) {
            return Token.String(string.length(), string.substring(1, string.length() - 1));
        }
    }

    private static class IntRecognizer extends PatternRecognizer<Integer> {
        private IntRecognizer() {
            this("[+-]?\\d+");
        }

        private IntRecognizer(String value) {
            super(value);
        }

        @Override
        protected Token<Integer> matched(String string) {
            return Token.Int(string.length(), Integer.valueOf(string));
        }
    }

    private static class HexadecimalRecognizer extends PatternRecognizer<Integer> {
        private HexadecimalRecognizer() {
            super("0[xX][a-fA-F0-9]+");
        }

        @Override
        protected Token<Integer> matched(String string) {
            return Token.Int(string.length(), Integer.valueOf(string.substring(2, string.length()), 16));
        }
    }

    private static class FloatRecognizer extends PatternRecognizer<Float> {
        private FloatRecognizer() {
            super("[-+]?\\d*\\.(\\d+([eE][-+]?\\d+)?)?");
        }

        @Override
        protected Token<Float> matched(String string) {
            return Token.Float(string.length(), Float.valueOf(string));
        }
    }

    public static class Skip extends PatternRecognizer {
        private Skip(String value) {
            super(value);
        }

        @Override
        protected Token<String> matched(String string) {
            return Token.String(string.length(), string);
        }
    }
}
