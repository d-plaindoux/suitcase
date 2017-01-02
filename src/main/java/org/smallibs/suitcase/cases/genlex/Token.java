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

public abstract class Token<T> {

    private final T value;
    private final int length;

    private Token(T value, int length) {
        this.value = value;
        this.length = length;
    }

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

    public static Token<Float> Float(int length, float value) {
        return new FloatToken(length, value);
    }

    public static <T> Token<T> Generic(String kind, int length, T value) {
        return new GenericToken<>(kind, length, value);
    }

    public T value() {
        return this.value;
    }

    public int length() {
        return this.length;
    }

    //
    // Implementations
    //

    public static class KeywordToken extends Token<String> {
        private KeywordToken(String value) {
            super(value, value.length());
        }
    }

    public static class GenericToken<T> extends Token<T> {
        private final String kind;

        public GenericToken(String kind, int length, T value) {
            super(value, length);
            this.kind = kind;
        }

        public String kind() {
            return kind;
        }
    }

    public static class IdentToken extends Token<String> {
        private IdentToken(String value) {
            super(value, value.length());
        }
    }

    public static class StringToken extends Token<String> {
        private StringToken(int length, String value) {
            super(value, length);
        }
    }

    public static class IntToken extends Token<Integer> {
        private IntToken(int length, int value) {
            super(value, length);
        }
    }

    public static class FloatToken extends Token<Float> {
        private FloatToken(int length, float value) {
            super(value, length);
        }
    }
}
