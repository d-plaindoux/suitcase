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

package org.smallibs.suitcase.pattern.core;

import org.smallibs.suitcase.pattern.Cases;

public abstract class Case1<T, R> implements Case<T, R> {

    protected final Case<R, ?> _1;

    protected Case1() {
        this._1 = null;
    }

    protected Case1(Object o1) {
        this._1 = Cases.reifyAll(o1);
    }

}
