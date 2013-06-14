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

package org.smallibs.suitcase.pattern;

import org.smallibs.suitcase.pattern.core.Any;
import org.smallibs.suitcase.pattern.core.Constant;
import org.smallibs.suitcase.pattern.core.Null;
import org.smallibs.suitcase.pattern.core.TypeOf;
import org.smallibs.suitcase.pattern.prototype.Case1;

public final class Cases {

    public static Any _ = new Any();

    private Cases() {
        // Prevent useless creation
    }

    public static <T> Case1<T, ?> reify(Object value) {
        if (value == null) return nil();
        else if (value.equals(_)) return any();
        else if (value instanceof Class) return Cases.typeOf((Class<?>) value);
        else if (value instanceof Case1) return (Case1<T, ?>) value;
        else return Cases.constant((T) value);
    }

    public static <T> Case1<T, T> constant(T value) {
        assert value != null;
        return new Constant<>(value);
    }

    public static <T> Case1<T, T> nil() {
        return new Null<>();
    }

    public static <T> Case1<T, T> any() {
        return new Any<>();
    }

    public static <T, R> Case1<T, R> typeOf(Class<R> type) {
        assert type != null;
        return new TypeOf<>(type);
    }

}
