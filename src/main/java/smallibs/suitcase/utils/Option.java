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

    public static <T> Option<T> None() {
        return new NoneCase<>();
    }

    public static <T> Option<T> Some(T value) {
        return new SomeCase<>(value);
    }

    abstract public T value();

    abstract public boolean isNone();

    public boolean isSome() {
        return !isNone();
    }

    public static class NoneCase<T> extends Option<T> {

        @Override
        public T value() {
            return null;
        }

        @Override
        public boolean isNone() {
            return true;
        }
    }

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
        public boolean isNone() {
            return false;
        }
    }

}
