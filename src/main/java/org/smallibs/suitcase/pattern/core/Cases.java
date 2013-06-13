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

package org.smallibs.suitcase.pattern.core;

public final class Cases {

    public static Object _ = new Object();

    private Cases() {
        // Prevent useless creation
    }

    @SuppressWarnings("unchecked")
    public static <T> Case<T, T> reify(Object value) {
        if (value == null) return nil();
        else if (value instanceof Case) return (Case<T, T>) value;
        else if (value.equals(_)) return _();
        else if (value instanceof Class) return typeOf((Class<T>) value);
        else return (Case<T, T>) constant(value);
    }

    public static <T> Case<T, T> constant(T value) {
        assert value != null;
        return new Constant<T>(value);
    }

    public static <T> Case<T, T> nil() {
        return new Null<T>();
    }

    public static <T> Case<T, T> _() {
        return new Any<T>();
    }

    public static <T, R> Case<T, R> typeOf(Class<R> type) {
        assert type != null;
        return new OfType<T, R>(type);
    }

}
