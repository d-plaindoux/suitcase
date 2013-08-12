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
import smallibs.suitcase.cases.core.ReentrantMatcher;
import smallibs.suitcase.cases.core.Var;
import smallibs.suitcase.match.Matcher;
import smallibs.suitcase.utils.Option;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.core.Cases.fromObject;

public class Parser {

    public static <T> ReentrantMatcher<TokenStream, T> parser(Matcher<TokenStream, T> matcher) {
        return new ReentrantParser<>(matcher);
    }

    public static <T> ReentrantMatcher<TokenStream, T> parser(Matcher<TokenStream, T> matcher, Lexer lexer) {
        return new ReentrantParser<>(matcher, lexer);
    }

    public static Case<TokenStream> Seq(Object... seq) {
        return new SeqCase(seq);
    }

    public static Case<TokenStream> Opt(Object... seq) {
        if (seq.length == 1) {
            return new OptCase(seq[0]);
        } else {
            return new OptCase(new SeqCase(seq));
        }
    }

    public static Case<TokenStream> Alt(Object... alternatives) {
        return new AltCase(alternatives);
    }

    public static Case<TokenStream> Kwd = Kwd(_);

    public static Case<TokenStream> Kwd(Object object) {
        return new KeywordCase(object);
    }

    public static Case<TokenStream> Ident = Ident(_);

    public static Case<TokenStream> Ident(Object aCase) {
        return new IdentCase(aCase);
    }

    public static Case<TokenStream> Int = Int(_);

    public static Case<TokenStream> Int(Object aCase) {
        return new IntCase(aCase);
    }

    public static Case<TokenStream> Float = Float(_);

    public static Case<TokenStream> Float(Object aCase) {
        return new FloatCase(aCase);
    }

    public static Case<TokenStream> String = String(_);

    public static Case<TokenStream> String(Object aCase) {
        return new StringCase(aCase);
    }
    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
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

    public static abstract class PrimitiveCase<T> implements TokenStreamCase {
        private final Class<? extends Token<T>> type;
        private final Case<T> value;

        public PrimitiveCase(Class<? extends Token<T>> type, Object object) {
            this.type = type;
            this.value = fromObject(object);
        }

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

        private Option<MatchResult> unapplyToken(Token token) {
            if (this.type.isAssignableFrom(token.getClass())) {
                final Token<T> castedToken = type.cast(token);
                final Option<MatchResult> unapply = this.value.unapply(castedToken.value());
                if (unapply.isSome()) {
                    return Option.Some(new MatchResult(token).with(unapply.value()));
                }
            }

            return Option.None();
        }

        @Override
        public List<Class> variableTypes() {
            return this.value.variableTypes();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class IdentCase extends PrimitiveCase<String> {
        public IdentCase(Object object) {
            super(Token.IdentToken.class, object);
        }
    }

    @CaseType(TokenStream.class)
    private static class KeywordCase extends PrimitiveCase<String> {
        public KeywordCase(Object object) {
            super(Token.KeywordToken.class, object);
        }
    }

    @CaseType(TokenStream.class)
    private static class IntCase extends PrimitiveCase<Integer> {
        public IntCase(Object object) {
            super(Token.IntToken.class, object);
        }
    }


    @CaseType(TokenStream.class)
    private static class StringCase extends PrimitiveCase<String> {
        public StringCase(Object object) {
            super(Token.StringToken.class, object);
        }
    }

    @CaseType(TokenStream.class)
    private static class FloatCase extends PrimitiveCase<Float> {
        public FloatCase(Object object) {
            super(Token.FloatToken.class, object);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class ReentrantParser<R> extends ReentrantMatcher<TokenStream, R> implements TokenStreamCase {
        private final Option<Lexer> lexer;

        public ReentrantParser(Matcher<TokenStream, R> matcher) {
            super(matcher);
            this.lexer = Option.None();
        }

        public ReentrantParser(Matcher<TokenStream, R> matcher, Lexer lexer) {
            super(matcher);
            this.lexer = Option.Some(lexer);
        }

        @Override
        public Option<MatchResult> unapply(TokenStream stream) {
            if (lexer.isNone()) {
                return super.unapply(stream);
            } else {
                final Lexer lexer = stream.setLexer(this.lexer.value());
                try {
                    return super.unapply(stream);
                } finally {
                    stream.setLexer(lexer);
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class SeqCase implements TokenStreamCase {
        private final List<Case<?>> cases;

        public SeqCase(Object[] seq) {
            this.cases = new ArrayList<>();
            for (Object o : seq) {
                final Case<?> aCase = fromObject(o);
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

        @Override
        public List<Class> variableTypes() {
            final List<Class> classes = new ArrayList<>();

            for (Case<?> aCase : this.cases) {
                classes.addAll(aCase.variableTypes());
            }

            return classes;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class AltCase implements TokenStreamCase {
        private final List<Case<?>> cases;

        public AltCase(Object[] alternatives) {
            this.cases = new ArrayList<>();
            for (Object o : alternatives) {
                final Case<?> aCase = fromObject(o);
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

        @Override
        public List<Class> variableTypes() {
            final List<Class> classes = new ArrayList<>();

            for (Case<?> aCase : this.cases) {
                final List<Class> classList = aCase.variableTypes();
                if (classList.size() > classes.size()) {
                    classes.clear();
                    classes.addAll(classList);
                }
            }

            return classes;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @CaseType(TokenStream.class)
    private static class OptCase implements TokenStreamCase {
        private final Case<TokenStream> aCase;

        public OptCase(Object object) {
            this.aCase = fromObject(object);
        }

        @Override
        public Option<MatchResult> unapply(TokenStream tokenStream) {
            final TokenStream secundary = tokenStream.secundary();
            final Option<MatchResult> unapply = aCase.unapply(secundary);

            final MatchResult result;

            if (unapply.isSome()) {
                tokenStream.synchronizeWith(secundary);
                result = new MatchResult(Option.Some(unapply.value().matchedObject()));
                final List<Object> bindings = unapply.value().bindings();
                for (Object o : bindings) {
                    result.with(new MatchResult(Option.Some(o), null));
                }
            } else {
                // Simulate in order to collect variables
                result = new MatchResult(Option.None());
                final List<Class> classes = aCase.variableTypes();
                for (Class _ : classes) {
                    result.with(new MatchResult(Option.None(), null));
                }
            }

            return matchFully(tokenStream, Option.Some(result));
        }

        @Override
        public List<Class> variableTypes() {
            return aCase.variableTypes();
        }
    }
}
