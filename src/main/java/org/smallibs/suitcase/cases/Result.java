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

package org.smallibs.suitcase.cases;

public abstract class Result<R> {

    public static <R> WithoutCapture<R> success(R term) {
        return new WithoutCapture<>(term);
    }

    public static <C> WithCapture<C> successWithCapture(C result) {
        return new WithCapture<>(result);
    }

    private final R capturedObject;

    public Result(R capturedObject) {
        this.capturedObject = capturedObject;
    }

    public R resultValue() {
        return capturedObject;
    }

    public static class WithCapture<C> extends Result<C> {
        public WithCapture(C capturedObject) {
            super(capturedObject);
        }
    }

    public static class WithoutCapture<T> extends Result<T> {
        public WithoutCapture(T capturedObject) {
            super(capturedObject);
        }
    }

}
