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

package smallibs.suitcase.cases.genlex;

import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.match.Matcher;
import smallibs.suitcase.match.MatchingException;
import smallibs.suitcase.utils.Option;

public class Parser {

    public static <T> ParserCase<T> parser(Matcher<TokenStream, T> matcher) {
        return new ParserCase<>(matcher);
    }

    public static Case<TokenStream> Seq(Object... seq) {
        return null;
    }

    public static Case<TokenStream> Opt(Object... seq) {
        return null;
    }

    public static Case<TokenStream> Alt(Object... seq) {
        return null;
    }

    public static Case<TokenStream> Kwd(Object object) {
        return null;
    }

    public static Case<TokenStream> Int = null;

    public static Case<TokenStream> Ident = null;

    public static Case<TokenStream> Float = null;

    public static Case<TokenStream> String = null;

    public static Case<TokenStream> Char = null;

    // -----------------------------------------------------------------------------------------------------------------

    public static class ParserCase<T> extends Matcher<TokenStream, T> implements Case<TokenStream> {

        private final Matcher<TokenStream, T> matcher;

        public ParserCase(Matcher<TokenStream, T> matcher) {
            this.matcher = matcher;
        }

        @Override
        public Option<MatchResult> unapply(TokenStream stream) {
            return new Option.Some(new MatchResult(matcher.match(stream)));
        }

        public static <T1, R> Matcher<T1, R> create() {
            return Matcher.create();
        }

        @Override
        public When caseOf(Object object) {
            return matcher.caseOf(object);
        }

        @SuppressWarnings("unchecked")
        public T match(TokenStream object) throws MatchingException {
            return matcher.match(object);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------


}
