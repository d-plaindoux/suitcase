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
import org.smallibs.suitcase.pattern.prototype.Case2;
import org.smallibs.suitcase.utils.Tuple2;
import org.smallibs.suitcase.utils.Option;

import java.util.LinkedList;
import java.util.List;

@CaseType(List.class)
public class Cons<E> extends Case2<List<E>, E, List<E>> {

    public Cons(Object o1, Object o2) {
        super(o1, o2);
    }

    // Missing apply capability for construction

    public Option<Tuple2<E, List<E>>> unapply(List<E> list) {
        if (!list.isEmpty()) {
            final List<E> tail = new LinkedList<E>(list);
            final Option<E> headResult = this._1.unapply(tail.remove(0));

            if (!headResult.isNone()) {
                final Option<List<E>> tailResult = this._2.unapply(tail);
                if (!tailResult.isNone()) {
                    return new Option.Some<Tuple2<E, List<E>>>(new Tuple2<E, List<E>>(headResult.value(), tailResult.value()));
                }
            }
        }

        return new Option.None<Tuple2<E, List<E>>>();
    }

}
