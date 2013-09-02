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

public abstract class Token<T> {

    public static Token<String> Keyword(String value) {
        return new KeywordToken(value);
    }

    public static Token<String> Ident(String value) {
        return new IdentToken(value);
    }

    public static Token<String> String(int length, String value) {
        return new StringToken(length, value);
    }

    public static Token<Integer> Int(int length, int value) {
        return new IntToken(length, value);
    }

    public static Token<Float> Float(int length, Float value) {
        return new FloatToken(length, value);
    }

    public static <T> Token<T> Generic(String kind, int length, T value) {
        return new GenericToken<>(kind, length, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public abstract T value();

    public abstract int length();

    // -----------------------------------------------------------------------------------------------------------------

    public static class KeywordToken extends Token<String> {
        private final String value;

        private KeywordToken(String value) {
            this.value = value;
        }

        @Override
        public int length() {
            return this.value.length();
        }

        @Override
        public String value() {
            return this.value;
        }
    }

    public static class GenericToken<T> extends Token<T> {
        private final String kind;
        private final int length;
        private final T value;

        public GenericToken(String kind, int length, T value) {
            this.kind = kind;
            this.length = length;
            this.value = value;
        }

        @Override
        public int length() {
            return this.length;
        }

        public String kind() {
            return kind;
        }

        @Override
        public T value() {
            return this.value;
        }
    }

    public static class IdentToken extends Token<String> {
        private final String value;

        private IdentToken(String value) {
            this.value = value;
        }

        @Override
        public int length() {
            return this.value.length();
        }

        @Override
        public String value() {
            return this.value;
        }
    }

    public static class StringToken extends Token<String> {
        private final int length;
        private final String value;

        private StringToken(int length, String value) {
            this.length = length;
            this.value = value;
        }

        @Override
        public int length() {
            return this.length;
        }

        @Override
        public String value() {
            return this.value;
        }
    }

    public static class IntToken extends Token<Integer> {
        private final int length;
        private final int value;

        private IntToken(int length, int value) {
            this.length = length;
            this.value = value;
        }

        @Override
        public int length() {
            return this.length;
        }

        @Override
        public Integer value() {
            return this.value;
        }
    }

    public static class FloatToken extends Token<Float> {
        private final int length;
        private final float value;

        private FloatToken(int length, float value) {
            this.length = length;
            this.value = value;
        }

        @Override
        public int length() {
            return this.length;
        }

        @Override
        public Float value() {
            return this.value;
        }
    }
}
