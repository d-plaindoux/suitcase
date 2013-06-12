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

public interface Option<T> {

    T value();

    boolean isNone();

    public class None<T> implements Option<T> {

        @Override
        public T value() {
            return null;
        }

        @Override
        public boolean isNone() {
            return true;
        }
    }

    public class Some<T> implements Option<T> {
        public final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public T value() {
            return this.value;
        }

        @Override
        public boolean isNone() {
            return false;
        }
    }

}
