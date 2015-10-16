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

package smallibs.suitcase.cases.genlex;

import smallibs.suitcase.utils.Option;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tokenizer {

    public static Tokenizer Kwd(String value) {
        return new KeywordRecognizer(value);
    }

    public static Tokenizer Ident(String value) {
        return new IdentifierRecognizer(value);
    }

    public static Tokenizer String() {
        return new StringRecognizer();
    }

    public static Tokenizer QuotedString() {
        return new QuotedStringRecognizer();
    }

    public static Tokenizer Int() {
        return new IntRecognizer();
    }

    public static Tokenizer Float() {
        return new FloatRecognizer();
    }

    public static Tokenizer Hexa() {
        return new HexadecimalRecognizer();
    }

    public static Skip Skip(String value) {
        return new Skip(value);
    }

    public static GenericRecognizer Generic(String kind, Tokenizer value) {
        return new GenericRecognizer(kind, value);
    }

    public static Tokenizer pattern(String value) {
        return new IdentifierRecognizer(value);
    }

    public static Tokenizer value(String value) {
        return new KeywordRecognizer(value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Public abstract methods
    // -----------------------------------------------------------------------------------------------------------------

    public abstract Option<Token<?>> recognize(CharSequence sequence);

    // -----------------------------------------------------------------------------------------------------------------
    // Private classes
    // -----------------------------------------------------------------------------------------------------------------

    private static class KeywordRecognizer extends Tokenizer {
        private final String value;

        private KeywordRecognizer(String value) {
            this.value = value;
        }

        @Override
        public Option<Token<?>> recognize(CharSequence sequence) {
            if (value.length() <= sequence.length() && value.contentEquals(sequence.subSequence(0, value.length()))) {
                return new Option.SomeCase<>(Token.Keyword(value));
            } else {
                return Option.None();
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static abstract class PatternRecognizer extends Tokenizer {
        private final Pattern pattern;

        protected PatternRecognizer(String value) {
            this.pattern = Pattern.compile("^" + value);

            // Prevent empty pattern recognition
            if (this.pattern.matcher("").matches()) {
                throw new IllegalArgumentException();
            }
        }

        protected abstract Token<?> matched(String string);

        @Override
        public Option<Token<?>> recognize(CharSequence sequence) {
            final Matcher matcher = pattern.matcher(sequence);
            if (matcher.find()) {
                final CharSequence recognized = sequence.subSequence(matcher.start(), matcher.end());
                return new Option.SomeCase<>(matched(recognized.toString()));
            } else {
                return Option.None();
            }
        }
    }

    public static class GenericRecognizer extends Tokenizer {
        private final String kind;
        private final Tokenizer value;

        public GenericRecognizer(String kind, Tokenizer value) {
            this.kind = kind;
            this.value = value;
        }

        @Override
        public Option<Token<?>> recognize(CharSequence sequence) {
            final Option<Token<?>> option = value.recognize(sequence);
            if (option.isPresent()) {
                final Token<?> value = option.value();
                final Token<?> generic = Token.Generic(kind, value.length(), value.value());
                return Option.Some(generic);
            } else {
                return Option.None();
            }

        }
    }

    private static class IdentifierRecognizer extends PatternRecognizer {
        public IdentifierRecognizer(String value) {
            super(value);
        }

        @Override
        protected Token<String> matched(String string) {
            return Token.Ident(string);
        }
    }

    private static class StringRecognizer extends PatternRecognizer {
        private StringRecognizer() {
            super("\"(\\\\.|[^\"])*\"");
        }

        @Override
        protected Token<String> matched(String string) {
            return Token.String(string.length(), string.substring(1, string.length() - 1));
        }
    }

    private static class QuotedStringRecognizer extends PatternRecognizer {
        private QuotedStringRecognizer() {
            super("'(\\.|[^'])*'");
        }

        @Override
        protected Token<String> matched(String string) {
            return Token.String(string.length(), string.substring(1, string.length() - 1));
        }
    }

    private static class IntRecognizer extends PatternRecognizer {
        private IntRecognizer() {
            super("[+-]?\\d+");
        }

        private IntRecognizer(String value) {
            super(value);
        }

        @Override
        protected Token<Integer> matched(String string) {
            return Token.Int(string.length(), Integer.valueOf(string));
        }
    }

    private static class HexadecimalRecognizer extends PatternRecognizer {
        private HexadecimalRecognizer() {
            super("0[xX][a-fA-F0-9]+");
        }

        @Override
        protected Token<Integer> matched(String string) {
            return Token.Int(string.length(), Integer.valueOf(string.substring(2, string.length()), 16));
        }
    }

    private static class FloatRecognizer extends PatternRecognizer {
        private FloatRecognizer() {
            super("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
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
