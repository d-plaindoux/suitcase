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

import java.util.Objects;
import java.util.Optional;

public interface Cases {

    static <T> Var.WithoutInnerCapture<T, T> Var() {
        return Var(Any());
    }

    static <T> Var.WithoutInnerCapture<T, T> Var(T value) {
        return Var(Constant(value));
    }

    static <T> Var.WithoutInnerCapture<T, T> Var(Class<T> value) {
        return Var(typeOf(value));
    }

    static <T, R> Var.WithoutInnerCapture<T, R> Var(Case.WithoutCapture<T, R> value) {
        return new Var.WithoutInnerCapture<>(value);
    }

    static <T, R> Var.WithInnerCapture<T, R> Var(Case.WithCapture<T, R> value) {
        return new Var.WithInnerCapture<>(value);
    }

    static <T> Case.WithoutCapture<T, T> Constant(T value) {
        return new Case0<T, T>(p -> Objects.deepEquals(p, value) ? Optional.of(p) : Optional.empty()).$();
    }

    static <T> Case.WithoutCapture<T, T> Null() {
        return new Null<>();
    }

    static <T> Case.WithoutCapture<T, T> Any() {
        return new Case0<T, T>(Optional::of).$();
    }

    static <T> Case.WithoutCapture<T, T> typeOf(Class<T> type) {
        return new Case0<T, T>(p -> p.getClass().isAssignableFrom(type) ? Optional.of(type.cast(p)) : Optional.empty()).$();
    }
}
