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

public final class Functions {

    public static <T, R> Function<T, R> constant(final R object) {
        return new Function<T, R>() {
            @Override
            public R apply(T any) {
                return object;
            }
        };
    }

    public static <R> Function<Void, R> function(final Function0<R> f) {
        return new Function<Void, R>() {
            @Override
            public R apply(Void p) {
                return f.apply();
            }
        };
    }

    public static <A, B, R> Function<Pair<A, B>, R> function(final Function2<A, B, R> f) {
        return new Function<Pair<A, B>, R>() {
            @Override
            public R apply(Pair<A, B> p) {
                return f.apply(p._1, p._2);
            }
        };
    }

    public static <A, B, C, R> Function<Pair<A, Pair<B, C>>, R> function(final Function3<A, B, C, R> f) {
        return new Function<Pair<A, Pair<B, C>>, R>() {
            @Override
            public R apply(Pair<A, Pair<B, C>> p) {
                return f.apply(p._1, p._2._1, p._2._2);
            }
        };
    }

    public static <A, B, C, D, R> Function<Pair<A, Pair<B, Pair<C, D>>>, R> function(final Function4<A, B, C, D, R> f) {
        return new Function<Pair<A, Pair<B, Pair<C, D>>>, R>() {
            @Override
            public R apply(Pair<A, Pair<B, Pair<C, D>>> p) {
                return f.apply(p._1, p._2._1, p._2._2._1, p._2._2._2);
            }
        };
    }
}
