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
        return new ReentrantParser<>(matcher);
    }

    public static Case<TokenStream> Seq(Object... seq) {
        return new Seq(seq);
    }

    public static Case<TokenStream> Opt(Object... seq) {
        if (seq.length == 1) {
            return new Opt(seq[0]);
        } else {
            return new Opt(new Seq(seq));
        }
    }

    public static Case<TokenStream> Alt(Object... alternatives) {
        return new Alt(alternatives);
    }

    public static Case<TokenStream> Kwd(Object object) {
        return new Keyword(object);
    }

    public static Case<TokenStream> Int = new IntCase();

    public static Case<TokenStream> Ident = new IdentCase();

    public static Case<TokenStream> Float = new FloatCase();

    public static Case<TokenStream> String = new StringCase();

    // -----------------------------------------------------------------------------------------------------------------

    private interface TokenStreamCase extends Case<TokenStream> {
        // Only for specification
    }

    private static boolean isTokenStreamCase(Case<?> aCase) {
        if (aCase instanceof Var) {
            return isTokenStreamCase(((Var<?>) aCase).getValue());
        } else {
            return aCase instanceof TokenStreamCase;
        }
    }

    private static Option<MatchResult> matchFully(TokenStream tokenStream, Option<MatchResult> result) {
        if (result.isSome() && tokenStream.isInitial() && !tokenStream.isEmpty()) {
            return Option.None();
        }

        return result;
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static abstract class AtomCase implements TokenStreamCase {

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            final TokenStream secundary = tokenStream.secundary();

            final Token token;
            try {
                token = secundary.nextToken();
            } catch (IOException | UnexpectedCharException e) {
                return Option.None();
            }

            final Option<MatchResult> resultOption = this.unapplyToken(token);
            if (resultOption.isNone()) {
                return Option.None();
            }

            tokenStream.synchronizeWith(secundary);

            return matchFully(tokenStream, Option.Some(new MatchResult(token).with(resultOption.value())));
        }

        abstract Option<MatchResult> unapplyToken(Token token);
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class IdentCase extends AtomCase {
        @Override
        Option<MatchResult> unapplyToken(Token token) {
            if (token instanceof Token.IdentToken) {
                return Option.Some(new MatchResult(token));
            } else {
                return Option.None();
            }
        }
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class Keyword extends AtomCase {
        private final Case<String> value;

        public Keyword(Object object) {
            this.value = Cases.fromObject(object);
        }

        @Override
        Option<MatchResult> unapplyToken(Token token) {
            if (token instanceof Token.KeywordToken) {
                final Token.KeywordToken keyword = Token.KeywordToken.class.cast(token);
                final Option<MatchResult> unapply = this.value.unapply(keyword.value());
                if (unapply.isSome()) {
                    return Option.Some(new MatchResult(token).with(unapply.value()));
                }
            }

            return Option.None();
        }
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class Seq implements TokenStreamCase {
        private final List<Case<?>> cases;

        public Seq(Object[] seq) {
            this.cases = new ArrayList<>();
            for (Object o : seq) {
                final Case<?> aCase = Cases.fromObject(o);
                this.cases.add(aCase);
            }
        }

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            final MatchResult result = new MatchResult(null);
            final List<Object> values = new ArrayList<>();
            final TokenStream secundary = tokenStream.secundary();

            for (Case<?> aCase : this.cases) {
                final Option<MatchResult> unapply;

                if (isTokenStreamCase(aCase)) {
                    final Case<TokenStream> streamCase = (Case<TokenStream>) aCase;
                    unapply = streamCase.unapply(secundary);
                } else {
                    try {
                        final Case<Token> tokenCase = (Case<Token>) aCase;
                        unapply = tokenCase.unapply(secundary.nextToken());
                    } catch (IOException | UnexpectedCharException e) {
                        return Option.None();
                    }
                }

                if (unapply.isNone()) {
                    return unapply;
                } else {
                    values.add(unapply.value().matchedObject());
                    result.with(unapply.value());
                }
            }

            tokenStream.synchronizeWith(secundary);

            return matchFully(tokenStream, Option.Some(new MatchResult(values).with(result)));
        }
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class Alt implements TokenStreamCase {
        private final List<Case<?>> cases;

        public Alt(Object[] alternatives) {
            this.cases = new ArrayList<>();
            for (Object o : alternatives) {
                final Case<?> aCase = Cases.fromObject(o);
                this.cases.add(aCase);
            }
        }

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            for (Case<?> aCase : this.cases) {
                final TokenStream secundary = tokenStream.secundary();
                final Option<MatchResult> unapply;

                if (isTokenStreamCase(aCase)) {
                    final Case<TokenStream> streamCase = (Case<TokenStream>) aCase;
                    unapply = streamCase.unapply(secundary);
                } else {
                    try {
                        final Case<Token> tokenCase = (Case<Token>) aCase;
                        unapply = tokenCase.unapply(secundary.nextToken());
                    } catch (IOException | UnexpectedCharException e) {
                        return Option.None();
                    }
                }

                if (unapply.isSome()) {
                    tokenStream.synchronizeWith(secundary);
                    return matchFully(tokenStream, unapply);
                }
            }

            return Option.None();
        }
    }
// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class Opt implements TokenStreamCase {
        private final Case<TokenStream> aCase;

        public Opt(Object object) {
            this.aCase = Cases.fromObject(object);
        }

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            final TokenStream secundary = tokenStream.secundary();
            final Option<MatchResult> unapply = aCase.unapply(secundary);

            if (unapply.isSome()) {
                tokenStream.synchronizeWith(secundary);
                final Option<Object> returnedObject = Option.Some(unapply.value().matchedObject());
                return matchFully(tokenStream, Option.Some(new MatchResult(returnedObject).with(unapply.value())));
            } else {
                // Simulate in order to collect variables
                return Option.Some(new MatchResult(Option.None()));
            }
        }
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class IntCase extends AtomCase {
        @Override
        Option<MatchResult> unapplyToken(Token token) {
            if (token instanceof Token.IntToken) {
                return Option.Some(new MatchResult(token));
            } else {
                return Option.None();
            }
        }
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class StringCase extends AtomCase {
        @Override
        Option<MatchResult> unapplyToken(Token token) {
            if (token instanceof Token.StringToken) {
                return Option.Some(new MatchResult(token));
            } else {
                return Option.None();
            }
        }
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class FloatCase extends AtomCase {
        @Override
        Option<MatchResult> unapplyToken(Token token) {
            if (token instanceof Token.FloatToken) {
                return Option.Some(new MatchResult(token));
            } else {
                return Option.None();
            }
        }
    }

// -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class ReentrantParser<R> extends ReentrantMatcher<TokenStream, R> implements TokenStreamCase {
        public ReentrantParser(Matcher<TokenStream, R> matcher) {
            super(matcher);
        }
    }
}
