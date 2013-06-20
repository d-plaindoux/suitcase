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
import org.smallibs.suitcase.matching.core.CoreMatch;
import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.utils.Function;
import org.smallibs.suitcase.utils.Function0;
import org.smallibs.suitcase.utils.Function1;
import org.smallibs.suitcase.utils.Function2;
import org.smallibs.suitcase.utils.Function3;
import org.smallibs.suitcase.utils.FunctionN;
import org.smallibs.suitcase.utils.Functions;

import java.util.List;

public final class Match<T, R> extends AbstractMatch<T, R, Match<T, R>> {

    private final CoreMatch<Function, Function1<List<Object>, R>> applyMatcher;

    {
        applyMatcher = new CoreMatch<>();
        applyMatcher.when(Cases.var.of(Function0.class)).then(Functions.Nto0());
        applyMatcher.when(Cases.var.of(Function1.class)).then(Functions.Nto1());
        applyMatcher.when(Cases.var.of(Function2.class)).then(Functions.NTo2());
        applyMatcher.when(Cases.var.of(Function3.class)).then(Functions.Nto3());
        applyMatcher.when(Cases.var.of(FunctionN.class)).then(Functions.NtoN());
    }

    public static <T, R> Match<T, R> match() {
        return new Match<>();
    }

    @Override
    protected Match<T, R> self() {
        return this;
    }

    @Override
    protected R apply(Function function, List<Object> objects) throws MatchingException {
        return applyMatcher.apply(function).apply(objects);
    }
}