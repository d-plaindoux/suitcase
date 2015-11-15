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

package org.smallibs.suitcase.cases.core;

import java.util.Optional;
import java.util.function.Function;

public interface TypeCase {

    static <P, T extends P> Case0<P, Boolean> of(Class<T> type) {
        return new Case0<>(
                e -> e.getClass().isAssignableFrom(type) ? Optional.of(true) : Optional.empty()
        );
    }

    static <P, T extends P, E> Case1<P, P, E> of(Class<T> type, Function<T, E> get) {
        return new Case1<>(
                e -> e.getClass().isAssignableFrom(type) ? Optional.of(e) : Optional.empty(),
                e -> get.apply(type.cast(e))
        );
    }

    static <P, T extends P, E1, E2> Case2<P, P, E1, E2> of(Class<T> type, Function<T, E1> get1, Function<T, E2> get2) {
        return new Case2<>(
                e -> e.getClass().isAssignableFrom(type) ? Optional.of(e) : Optional.empty(),
                e -> get1.apply(type.cast(e)),
                e -> get2.apply(type.cast(e))
        );
    }

    static <P, T extends P, E1, E2, E3> Case3<P, P, E1, E2, E3> of(Class<T> type, Function<T, E1> get1, Function<T, E2> get2, Function<T, E3> get3) {
        return new Case3<>(
                e -> e.getClass().isAssignableFrom(type) ? Optional.of(e) : Optional.empty(),
                e -> get1.apply(type.cast(e)),
                e -> get2.apply(type.cast(e)),
                e -> get3.apply(type.cast(e))
        );
    }

    static <P, T extends P, E1, E2, E3, E4> Case4<P, P, E1, E2, E3, E4> of(Class<T> type, Function<T, E1> get1, Function<T, E2> get2, Function<T, E3> get3, Function<T, E4> get4) {
        return new Case4<>(
                e -> e.getClass().isAssignableFrom(type) ? Optional.of(e) : Optional.empty(),
                e -> get1.apply(type.cast(e)),
                e -> get2.apply(type.cast(e)),
                e -> get3.apply(type.cast(e)),
                e -> get4.apply(type.cast(e))
        );
    }
}
