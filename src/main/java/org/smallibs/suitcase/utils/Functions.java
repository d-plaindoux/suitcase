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

package org.smallibs.suitcase.utils;

import org.smallibs.suitcase.matching.MatchingException;

import java.util.List;

public final class Functions {

    public static <R> FunctionN<R> constant(final R object) {
        return new FunctionN<R>() {
            @Override
            public R apply(Object... parameters) throws MatchingException {
                return object;
            }
        };
    }

    private static <R> Function1<List<Object>, R> applyObjectsList(final Function0<R> acceptor) {
        return new Function1<List<Object>, R>() {
            public R apply(List<Object> parameters) throws MatchingException {
                return acceptor.apply();
            }
        };
    }

    private static <R> Function1<List<Object>, R> applyObjectsList(final Function1<Object, R> acceptor) {
        return new Function1<List<Object>, R>() {
            public R apply(List<Object> parameters) throws MatchingException {
                return acceptor.apply(parameters.get(0));
            }
        };
    }

    private static <R> Function1<List<Object>, R> applyObjectsList(final Function2<Object, Object, R> acceptor) {
        return new Function1<List<Object>, R>() {
            public R apply(List<Object> parameters) throws MatchingException {
                return acceptor.apply(parameters.get(0), parameters.get(1));
            }
        };
    }

    private static <R> Function1<List<Object>, R> applyObjectsList(final Function3<Object, Object, Object, R> acceptor) {
        return new Function1<List<Object>, R>() {
            public R apply(List<Object> parameters) throws MatchingException {
                return acceptor.apply(parameters.get(0), parameters.get(1), parameters.get(2));
            }
        };
    }

    private static <R> Function1<List<Object>, R> applyObjectsList(final FunctionN<R> acceptor) {
        return new Function1<List<Object>, R>() {
            public R apply(List<Object> parameters) throws MatchingException {
                final Object[] objects = parameters.toArray(new Object[parameters.size()]);
                return acceptor.apply(objects);
            }
        };
    }

    public static <R> Function1<Function0<R>, Function1<List<Object>, R>> Nto0() {
        return new Function1<Function0<R>, Function1<List<Object>, R>>() {
            public Function1<List<Object>, R> apply(final Function0<R> acceptor) throws MatchingException {
                return Functions.applyObjectsList(acceptor);
            }
        };
    }

    public static <R> Function1<Function1<Object, R>, Function1<List<Object>, R>> Nto1() {
        return new Function1<Function1<Object, R>, Function1<List<Object>, R>>() {
            public Function1<List<Object>, R> apply(final Function1<Object, R> acceptor) throws MatchingException {
                return Functions.applyObjectsList(acceptor);
            }
        };
    }

    public static <R> Function1<Function2<Object, Object, R>, Function1<List<Object>, R>> NTo2() {
        return new Function1<Function2<Object, Object, R>, Function1<List<Object>, R>>() {
            public Function1<List<Object>, R> apply(final Function2<Object, Object, R> acceptor) throws MatchingException {
                return Functions.applyObjectsList(acceptor);
            }
        };
    }

    public static <R> Function1<Function3<Object, Object, Object, R>, Function1<List<Object>, R>> Nto3() {
        return new Function1<Function3<Object, Object, Object, R>, Function1<List<Object>, R>>() {
            public Function1<List<Object>, R> apply(final Function3<Object, Object, Object, R> acceptor) throws MatchingException {
                return Functions.applyObjectsList(acceptor);
            }
        };
    }

    public static <R> Function1<FunctionN<R>, Function1<List<Object>, R>> NtoN() {
        return new Function1<FunctionN<R>, Function1<List<Object>, R>>() {
            public Function1<List<Object>, R> apply(final FunctionN<R> acceptor) throws MatchingException {
                return Functions.applyObjectsList(acceptor);
            }
        };
    }
}
