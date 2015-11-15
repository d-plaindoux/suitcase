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

import org.smallibs.suitcase.cases.Case.WithoutCapture;
import org.smallibs.suitcase.cases.Result;

import java.util.Optional;
import java.util.function.Function;

public class Case0<P, R> {

    private final Function<P, Optional<R>> predicate;

    public Case0(Function<P, Optional<R>> predicate) {
        this.predicate = predicate;
    }

    public WithoutCapture<P, R> $() {
        return WithoutCapture.adapt(new Pattern());
    }

    public class Pattern implements WithoutCapture<P, R> {
        public Pattern() {
        }

        @Override
        public Optional<Result.WithoutCapture<R>> unapply(P p) {
            return predicate.apply(p).flatMap(r -> Optional.of(Result.success(r)));
        }
    }
}
