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

package smallibs.suitcase.cases.core;

import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.match.Matcher;
import smallibs.suitcase.match.MatchingException;
import smallibs.suitcase.utils.Option;

import java.util.ArrayList;
import java.util.List;

public class ReentrantMatcher<E, T> extends Matcher<E, T> implements Case<E> {

    public static <E,T> Matcher<E, T> reentrantMatcher(Matcher<E, T> matcher) {
        return new ReentrantMatcher<>(matcher);
    }

    private final Matcher<E, T> matcher;

    protected ReentrantMatcher(Matcher<E, T> matcher) {
        super();
        this.matcher = matcher;
    }

    @Override
    public Option<MatchResult> unapply(E stream) {
        try {
            return Option.Some(new MatchResult(matcher.match(stream)));
        } catch (MatchingException e) {
            return Option.None();
        }
    }

    @Override
    public List<Class> variableTypes() {
        return new ArrayList<>();
    }

    @Override
    public When caseOf(Object object) {
        return matcher.caseOf(object);
    }

    @Override
    public T match(E object) throws MatchingException {
        return matcher.match(object);
    }
}
