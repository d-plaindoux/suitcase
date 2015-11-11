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

package org.smallibs.suitcase.utils;

import org.smallibs.suitcase.utils.Functions.Function2;
import org.smallibs.suitcase.utils.Functions.Function3;

import java.util.function.Function;

public interface Apply {

    static <C1, R> Function<C1, R> function(Function<C1, R> function) {
        return function;
    }

    static <C1, C2, R> Function<Pair<C1, C2>, R> function(Function2<C1, C2, R> function) {
        return params -> function.apply(params._1, params._2);
    }

    static <C1, C2, C3, R> Function<Pair<C1, Pair<C2, C3>>, R> function(Function3<C1, C2, C3, R> function) {
        return params -> function.apply(params._1, params._2._1, params._2._2);
    }

}
