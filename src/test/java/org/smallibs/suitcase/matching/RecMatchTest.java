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

import org.junit.Test;
import org.smallibs.suitcase.pattern.peano.Succ;
import org.smallibs.suitcase.pattern.peano.Zero;
import org.smallibs.suitcase.utils.Function;

import static org.smallibs.suitcase.pattern.Cases.var;

public class RecMatchTest {

    @Test(expected = StackOverflowError.class)
    public void shouldHaveStackOverflow() throws MatchingException {
        final Match<Integer, Integer> multiplyMatcher = Match.match();

        multiplyMatcher.when(new Zero()).then(0);
        multiplyMatcher.when(new Succ(var)).then(new Function<Integer, Integer>() {
            public Integer apply(Integer i) {
                return 1 + multiplyMatcher.apply(i);
            }
        });

        multiplyMatcher.apply(1000000);
    }

}
