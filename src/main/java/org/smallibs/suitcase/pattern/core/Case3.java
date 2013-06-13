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

import org.smallibs.suitcase.utils.Tuple3;

public abstract class Case3<T, R1, R2, R3> implements Case<T, Tuple3<R1, R2, R3>> {

    protected final Case<R1, R1> _1;
    protected final Case<R2, R2> _2;
    protected final Case<R3, R3> _3;

    protected Case3(Object o1, Object o2, Object o3) {
        this._1 = Cases.reify(o1);
        this._2 = Cases.reify(o2);
        this._3 = Cases.reify(o3);
    }

}
