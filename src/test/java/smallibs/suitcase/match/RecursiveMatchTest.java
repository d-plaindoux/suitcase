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

package smallibs.suitcase.match;

import org.junit.Test;
import smallibs.suitcase.utils.Function;

import static smallibs.suitcase.cases.core.Cases.var;
import static smallibs.suitcase.cases.peano.Peano.Succ;
import static smallibs.suitcase.cases.peano.Peano.Zero;

public class RecursiveMatchTest {

    @Test(expected = StackOverflowError.class)
    public void shouldHaveStackOverflow() throws MatchingException {
        final Matcher<Integer, Integer> multiplyMatcher = Matcher.create();

        multiplyMatcher.caseOf(Zero).then.value(0);
        multiplyMatcher.caseOf(Succ(var)).then.function(new Function<Integer, Integer>() {
            public Integer apply(Integer i) {
                return 1 + multiplyMatcher.match(i);
            }
        });

        multiplyMatcher.match(1000000);
    }

}