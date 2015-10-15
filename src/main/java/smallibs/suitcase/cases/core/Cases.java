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

package smallibs.suitcase.cases.core;

import smallibs.suitcase.cases.Case;

public final class Cases {

    public static AnyObject __ = new AnyObject();
    public static VariableObject var = new VariableObject();

    private Cases() {
        // Prevent useless creation
    }

    public static <T> Case<T> fromObject(final Object value) {
        if (value == null) return nil();
        else if (value.equals(__)) return any();
        else if (value.equals(var)) return var.of(Cases.<T>any());
        else if (value instanceof Class) return typeOf((Class<?>) value);
        else if (value instanceof Case) return (Case<T>) value;
        else return Cases.constant((T) value);
    }

    public static <T> Case<T> constant(T value) {
        assert value != null;
        return new Constant<>(value);
    }

    public static <T> Case<T> nil() {
        return new Null<>();
    }

    public static <T> Case<T> any() {
        return new Any<>();
    }

    public static <T> Case<T> typeOf(Class<?> type) {
        assert type != null;
        return new TypeOf<>(type);
    }

    public static class AnyObject {
        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof AnyObject;
        }

        @Override
        public int hashCode() {
            return 13;
        }
    }

    public static class VariableObject {
        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof VariableObject;
        }

        @Override
        public int hashCode() {
            return 13;
        }

        public <T> Case<T> of(T value) {
            return new Var<>(value);
        }

        public <T> Case<T> of(Class<? extends T> value) {
            return new Var<>(value);
        }

        public <T> Case<T> of(Case<T> value) {
            return new Var<>(value);
        }

        public <T> Case<T> of(AnyObject any) {
            return new Var<>(Cases.<T>any());
        }
    }

    public static class SequenceObject {
        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof AnyObject;
        }

        @Override
        public int hashCode() {
            return 13;
        }
    }
}
