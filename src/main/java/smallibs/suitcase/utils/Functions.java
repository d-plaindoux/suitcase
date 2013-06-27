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

    public static <M1, M2, R> Function<Pair<M1, M2>, R> function(final Function2<M1, M2, R> f) {
        return new Function<Pair<M1, M2>, R>() {
            @Override
            public R apply(Pair<M1, M2> p) {
                return f.apply(p._1, p._2);
            }
        };
    }

    public static <M1, M2, M3, R> Function<Pair<M1, Pair<M2, M3>>, R> function(final Function3<M1, M2, M3, R> f) {
        return new Function<Pair<M1, Pair<M2, M3>>, R>() {
            @Override
            public R apply(Pair<M1, Pair<M2, M3>> p) {
                return f.apply(p._1, p._2._1, p._2._2);
            }
        };
    }

    public static <M1, M2, M3, M4, R> Function<Pair<M1, Pair<M2, Pair<M3, M4>>>, R> function(final Function4<M1, M2, M3, M4, R> f) {
        return new Function<Pair<M1, Pair<M2, Pair<M3, M4>>>, R>() {
            @Override
            public R apply(Pair<M1, Pair<M2, Pair<M3, M4>>> p) {
                return f.apply(p._1, p._2._1, p._2._2._1, p._2._2._2);
            }
        };
    }
}
