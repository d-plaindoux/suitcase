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

public final class Functions {

    // =================================================================================================================
    // Constructors
    // =================================================================================================================

    private Functions() {
    }

    // =================================================================================================================
    // Static behaviors
    // =================================================================================================================

    public static <R> Function0<R> constant0(final R constant) {
        return new Function0<R>() {
            @Override
            public R apply() {
                return constant;
            }
        };
    }

    public static <M, R> Function1<M, R> constant1(final R constant) {
        return new Function1<M, R>() {
            @Override
            public R apply(M parameters) {
                return constant;
            }
        };
    }

    public static <M1, M2, R> Function2<M1, M2, R> constant2(final R constant) {
        return new Function2<M1, M2, R>() {
            @Override
            public R apply(M1 p1, M2 p2) {
                return constant;
            }
        };
    }

}
