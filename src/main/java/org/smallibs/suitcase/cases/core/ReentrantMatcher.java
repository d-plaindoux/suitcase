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
import org.smallibs.suitcase.cases.MatchResult;
import org.smallibs.suitcase.match.Matcher;
import org.smallibs.suitcase.match.MatchingException;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

public class ReentrantMatcher<E, T> extends Matcher<E, T> implements Case<E> {

    private final Matcher<E, T> matcher;

    protected ReentrantMatcher(Matcher<E, T> matcher) {
        super();
        this.matcher = matcher;
    }

    @Override
    public Optional<MatchResult> unapply(E stream) {
        try {
            return Optional.ofNullable(new MatchResult(matcher.match(stream)));
        } catch (MatchingException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Class> variableTypes() {
        return new ArrayList<>();
    }

    @Override
    public WhenRule caseOf(Case<? extends E> object) {
        return matcher.caseOf(object);
    }

    @Override
    public WhenRule caseOf(Class<? extends E> object) {
        return matcher.caseOf(object);
    }

    @Override
    public WhenRule caseOf(E object) {
        return matcher.caseOf(object);
    }

    @Override
    public T match(E object) throws MatchingException {
        return matcher.match(object);
    }
}
