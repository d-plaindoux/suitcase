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

    static <C1, R> Function<Apply1<C1>, R> function(Function<C1, R> function) {
        return apply -> apply.eval(function);
    }

    static <C1, C2, R> Function<Apply2<C1, C2>, R> function(Function2<C1, C2, R> function) {
        return apply -> apply.eval(function);
    }

    class Apply1<C1> {
        private final C1 c1;

        public Apply1(C1 c1) {
            this.c1 = c1;
        }

        public <R> R eval(Function<C1, R> function) {
            return function.apply(c1);
        }
    }

    class Apply2<C1, C2> extends Pair<C1, C2> {
        private final C1 c1;
        private final C2 c2;

        public Apply2(C1 c1, C2 c2) {
            super(c1, c2);
            this.c1 = c1;
            this.c2 = c2;
        }

        public <R> R eval(Function2<C1, C2, R> function) {
            return function.apply(c1, c2);
        }
    }

    class Apply3<C1, C2, C3> extends Pair<C1, Pair<C2, C3>> {
        private final C1 c1;
        private final C2 c2;
        private final C3 c3;

        public Apply3(C1 c1, C2 c2, C3 c3) {
            super(c1, new Pair<>(c2, c3));
            this.c1 = c1;
            this.c2 = c2;
            this.c3 = c3;
        }

        public <R> R eval(Function3<C1, C2, C3, R> function) {
            return function.apply(c1, c2, c3);
        }
    }
}
