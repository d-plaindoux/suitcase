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

import org.smallibs.suitcase.cases.Case;

public interface Cases {

    static <T> Var.WithoutCapture<T, T> Var() {
        return new Var.WithoutCapture<>(new Any<>());
    }

    static <T> Var.WithoutCapture<T, T> Var(T value) {
        return new Var.WithoutCapture<>(Constant(value));
    }

    static <T> Var.WithoutCapture<T, T> Var(Class<T> value) {
        return new Var.WithoutCapture<>(typeOf(value));
    }

    static <T, R> Var.WithoutCapture<T, R> Var(Case.WithoutCapture<T, R> value) {
        return new Var.WithoutCapture<>(value);
    }

    static <T, R> Var.WithCapture<T, R> Var(Case.WithCapture<T, R> value) {
        return new Var.WithCapture<>(value);
    }

    static <T> Case.WithoutCapture<T, T> Constant(T value) {
        return new Constant<>(value);
    }

    static <T> Case.WithoutCapture<T, T> Null() {
        return new Null<>();
    }

    static <T> Case.WithoutCapture<T, T> Any() {
        return new Any<>();
    }

    static <T> Case.WithoutCapture<T, T> typeOf(Class<T> type) {
        return new TypeOf<>(type);
    }

}
