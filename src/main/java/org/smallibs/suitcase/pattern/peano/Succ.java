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

package org.smallibs.suitcase.pattern.peano;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.pattern.prototype.Case1;
import org.smallibs.suitcase.utils.Option;

@CaseType(Integer.class)
public class Succ extends Case1<Integer, Integer> {

    public Succ(Object o1) {
        super(o1);
    }

    public Option<Integer> unapply(Integer integer) {
        if (integer > 0) {
            return this._1.unapply(integer - 1);
        } else {
            return new Option.None<>();
        }
    }
}

