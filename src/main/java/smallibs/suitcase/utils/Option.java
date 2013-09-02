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

package smallibs.suitcase.utils;

public abstract class Option<T> {

    public enum Kind {None, Some}

    public static <T> Option<T> None() {
        return new NoneCase<>();
    }

    public static <T> Option<T> Some(T value) {
        return new SomeCase<>(value);
    }

    final public boolean isNone() {
        switch (this.kind()) {
            case None:
                return true;
            case Some:
                return false;
            default:
                throw new IllegalArgumentException();
        }
    }

    final public boolean isSome() {
        return !isNone();
    }

    abstract public Kind kind();

    abstract public T value();

    // -----------------------------------------------------------------------------------------------------------------
    // Implementations
    // -----------------------------------------------------------------------------------------------------------------

    public static class NoneCase<T> extends Option<T> {

        @Override
        public T value() {
            return null;
        }

        @Override
        public Kind kind() {
            return Kind.None;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static class SomeCase<T> extends Option<T> {
        public final T value;

        public SomeCase(T value) {
            this.value = value;
        }

        @Override
        public T value() {
            return this.value;
        }

        @Override
        public Kind kind() {
            return Kind.Some;
        }
    }

}
