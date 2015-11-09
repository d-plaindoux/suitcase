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

package org.smallibs.suitcase.utils;

import java.util.function.Supplier;

public final class Functions {

    public static <R> Supplier<R> constant(final R object) {
        return () -> object;
    }

    public interface Function2<A, B, R> {
        R apply(A a, B b);
    }

    public interface Function3<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    public interface Function4<A, B, C, D, R> {
        R apply(A a, B b, C c, D d);
    }

    public interface Function5<A, B, C, D, E, R> {
        R apply(A a, B b, C c);
    }

    public interface Function6<A, B, C, D, E, F, R> {
        R apply(A a, B b, C c);
    }

    public interface Function7<A, B, C, D, E, F, G, R> {
        R apply(A a, B b, C c);
    }

    public interface Function8<A, B, C, D, E, F, G, H, R> {
        R apply(A a, B b, C c);
    }

    public interface Function9<A, B, C, D, E, F, G, H, I, R> {
        R apply(A a, B b, C c);
    }
}

