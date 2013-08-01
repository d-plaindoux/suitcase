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

import smallibs.suitcase.annotations.CaseType;
import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.cases.core.Cases;
import smallibs.suitcase.cases.core.ReentrantMatcher;
import smallibs.suitcase.cases.core.Var;
import smallibs.suitcase.match.Matcher;
import smallibs.suitcase.utils.Option;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static <T> ReentrantMatcher<TokenStream, T> parser(Matcher<TokenStream, T> matcher) {
        return Cases.reentrant(matcher);
    }

    public static Case<TokenStream> Seq(Object... seq) {
        return new Seq(seq);
    }

    public static Case<TokenStream> Opt(Object... seq) {
        return null;
    }

    public static Case<TokenStream> Alt(Object... seq) {
        return null;
    }

    public static Case<TokenStream> Kwd(Object object) {
        return new Keyword(object);
    }

    public static Case<TokenStream> Int = new IntCase();

    public static Case<TokenStream> Ident = new IdentCase();

    public static Case<TokenStream> Float = null;

    public static Case<TokenStream> String = null;

    public static Case<TokenStream> Char = null;

    // -----------------------------------------------------------------------------------------------------------------

    private interface TokenStreamCase extends Case<TokenStream> {
        // Only for specification
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class IdentCase implements TokenStreamCase {

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            final Token token;
            try {
                token = tokenStream.secundary().nextToken();
            } catch (IOException | UnexpectedCharException e) {
                return Option.None();
            }

            if (!(token instanceof Token.IdentToken)) {
                return Option.None();
            }

            try {
                tokenStream.nextToken();
            } catch (IOException | UnexpectedCharException e) {
                // Impossible
            }

            return Option.Some(new MatchResult(token));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class Keyword implements TokenStreamCase {
        private final Case<String> value;

        public Keyword(Object object) {
            this.value = Cases.fromObject(object);
        }

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            final Token token;
            try {
                token = tokenStream.secundary().nextToken();
            } catch (IOException | UnexpectedCharException e) {
                return Option.None();
            }

            if (!(token instanceof Token.KeywordToken)) {
                return Option.None();
            }

            try {
                tokenStream.nextToken();
            } catch (IOException | UnexpectedCharException e) {
                // Impossible
            }

            final Token.KeywordToken keyword = Token.KeywordToken.class.cast(token);
            final Option<MatchResult> unapply = this.value.unapply(keyword.value());

            if (unapply.isNone()) {
                return unapply;
            }

            return Option.Some(new MatchResult(token).with(unapply.value()));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class Seq implements TokenStreamCase {
        private final List<Case<TokenStream>> cases;

        public Seq(Object[] seq) {
            this.cases = new ArrayList<>();
            for (Object o : seq) {
                final Case<TokenStream> aCase = Cases.fromObject(o);
                this.cases.add(aCase);
            }
        }

        private boolean isTokenStreamCase(Case<TokenStream> aCase) {
            if (aCase instanceof Var) {
                return isTokenStreamCase(((Var<TokenStream>) aCase).getValue());
            } else {
                return aCase instanceof TokenStreamCase;
            }
        }

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            final TokenStream secundary = tokenStream.secundary();
            final MatchResult result = new MatchResult(null);

            for (Case<TokenStream> aCase : this.cases) {
                final Option<MatchResult> unapply;

                if (isTokenStreamCase(aCase)) {
                    unapply = aCase.unapply(secundary);
                } else {
                    throw new IllegalArgumentException();
                }

                if (unapply.isNone()) {
                    return unapply;
                }

                result.with(unapply.value());
            }

            return Option.Some(result);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class IntCase implements TokenStreamCase {

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            final Token token;
            try {
                token = tokenStream.nextToken();
            } catch (IOException | UnexpectedCharException e) {
                return Option.None();
            }

            if (token instanceof Token.IntToken) {
                return Option.Some(new MatchResult(token));
            } else {
                return Option.None();
            }
        }
    }
}
