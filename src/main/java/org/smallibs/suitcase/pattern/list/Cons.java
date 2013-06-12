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

package org.smallibs.suitcase.pattern.list;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.utils.Couple;
import org.smallibs.suitcase.utils.Option;

import java.util.LinkedList;
import java.util.List;

@CaseType(List.class)
public class Cons<E> implements Case<List<E>, Couple<E, List<E>>> {
    public Option<Couple<E, List<E>>> unapply(List<E> list) {
        if (list.isEmpty()) {
            return new Option.None<Couple<E, List<E>>>();
        } else {
            final java.util.List<E> tail = new LinkedList<E>(list);
            final E head = tail.remove(0);
            return new Option.Some<Couple<E, List<E>>>(new Couple<E, List<E>>(head, tail));
        }
    }
}
