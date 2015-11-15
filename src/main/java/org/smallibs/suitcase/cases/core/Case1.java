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

package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.Case.WithCapture;
import org.smallibs.suitcase.cases.Case.WithoutCapture;

import java.util.Optional;
import java.util.function.Function;

public class Case1<P, R, E> {

    private final Function<P, Optional<R>> predicate;
    private final Function<R, E> compute;

    public Case1(Function<P, Optional<R>> predicate, Function<R, E> compute) {
        this.predicate = predicate;
        this.compute = compute;
    }

    public <C> WithoutCapture<P, C> $(WithoutCapture<E, C> aCase) {
        return WithoutCapture.adapt(new Pattern<>(aCase));
    }

    public <C> WithCapture<P, C> $(WithCapture<E, C> aCase) {
        return WithCapture.adapt(new Pattern<>(aCase));
    }

    public class Pattern<C> implements Case<P, C> {
        private final Case<E, C> aCase;

        public Pattern(Case<E, C> aCase) {
            this.aCase = aCase;
        }

        @Override
        public Optional<C> unapply(P e) {
            return predicate.apply(e).flatMap(r -> aCase.unapply(compute.apply(r)));
        }
    }
}