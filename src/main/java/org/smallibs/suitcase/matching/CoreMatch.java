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

package org.smallibs.suitcase.matching;

import org.smallibs.suitcase.matching.core.AbstractMatch;
import org.smallibs.suitcase.utils.Function;
import org.smallibs.suitcase.utils.Function0;
import org.smallibs.suitcase.utils.Function1;
import org.smallibs.suitcase.utils.Function2;
import org.smallibs.suitcase.utils.Function3;
import org.smallibs.suitcase.utils.Function4;
import org.smallibs.suitcase.utils.FunctionN;

import java.util.List;

final class CoreMatch<T, R> extends AbstractMatch<T, R, CoreMatch<T, R>> {

    CoreMatch() {
        // Package protected
    }

    @Override
    protected CoreMatch<T, R> self() {
        return this;
    }

    @Override
    protected R apply(Function function, List<Object> result) throws MatchingException {
        try {
            return ((Function1<Object, R>) function).apply(result.get(0));
        } catch (ClassCastException e) {
            throw new IllegalStateException(e);
        }
    }

}