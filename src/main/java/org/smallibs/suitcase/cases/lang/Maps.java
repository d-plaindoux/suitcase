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

package org.smallibs.suitcase.cases.lang;

import org.smallibs.suitcase.cases.Case;

import java.util.Map;
import java.util.Optional;

import static org.smallibs.suitcase.cases.core.Cases.Constant;

public interface Maps {

    static <T1, T2> Case.WithoutCapture<Map<T1, T2>, T2> Entry(T1 o1, T2 o2) {
        return Entry(o1, Constant(o2));
    }

    static <T1, T2, C> Case.WithoutCapture<Map<T1, T2>, C> Entry(T1 o1, Case.WithoutCapture<T2, C> o2) {
        return map -> Optional.ofNullable(map.get(o1)).flatMap(o2::unapply);
    }

    static <T1, T2, C> Case.WithCapture<Map<T1, T2>, C> Entry(T1 o1, Case.WithCapture<T2, C> o2) {
        return map -> Optional.ofNullable(map.get(o1)).flatMap(o2::unapply);
    }

}
