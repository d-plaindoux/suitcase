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
import org.smallibs.suitcase.cases.Result;
import org.smallibs.suitcase.utils.Pair;

import java.util.Optional;

public interface Var {

    class WithInnerCapture<T, R> implements Case.WithCapture<T, Pair<T, R>> {

        private final Case.WithCapture<T, R> aCase;

        public WithInnerCapture(Case.WithCapture<T, R> aCase) {
            this.aCase = aCase;
        }

        @Override
        public Optional<Result.WithCapture<Pair<T, R>>> unapply(T t) {
            return this.aCase.unapply(t).map(r -> Result.successWithCapture(new Pair<>(t, r.resultValue())));
        }
    }

    class WithoutInnerCapture<T, R> implements Case.WithCapture<T, R> {

        private final Case.WithoutCapture<T, R> aCase;

        public WithoutInnerCapture(Case.WithoutCapture<T, R> aCase) {
            this.aCase = aCase;
        }

        @Override
        public Optional<Result.WithCapture<R>> unapply(T t) {
            return this.aCase.unapply(t).map(result -> Result.successWithCapture(result.resultValue()));
        }
    }

}
