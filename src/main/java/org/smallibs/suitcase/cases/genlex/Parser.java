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

package org.smallibs.suitcase.cases.genlex;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;
import org.smallibs.suitcase.cases.core.ReentrantMatcher;
import org.smallibs.suitcase.cases.core.Var;
import org.smallibs.suitcase.match.Matcher;
import java.util.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.smallibs.suitcase.cases.core.Cases.__;
import static org.smallibs.suitcase.cases.core.Cases.fromObject;

public class Parser {

    public static Case<TokenStream> Kwd = Kwd(__);
    public static Case<TokenStream> Ident = Ident(__);
    public static Case<TokenStream> Int = Int(__);
    public static Case<TokenStream> Float = Float(__);
    public static Case<TokenStream> String = String(__);

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

    public static Case<TokenStream> Kwd(Object object) {
        return new KeywordCase(object);
    }

    public static Case<TokenStream> Ident(Object aCase) {
        return new IdentCase(aCase);
    }

    public static Case<TokenStream> Int(Object aCase) {
        return new IntCase(aCase);
    }

    public static Case<TokenStream> Float(Object aCase) {
        return new FloatCase(aCase);
    }

    public static Case<TokenStream> String(Object aCase) {
        return new StringCase(aCase);
    }

    public static GenericCase A(String kind, Object aCase) {
        return new GenericCase(kind, aCase);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isTokenStreamCase(Case<?> aCase) {
        if (aCase instanceof Var) {
            return isTokenStreamCase(((Var<?>) aCase).getValue());
        } else {
            return aCase instanceof TokenStreamCase;
        }
    }

    private static Optional<MatchResult> matchFully(TokenStream tokenStream, Optional<MatchResult> result) {
        if (result.isPresent() && tokenStream.isInitial() && !tokenStream.isEmpty()) {
            return Optional.empty();
        }

        return result;
    }

    @CaseType(TokenStream.class)
    private interface TokenStreamCase extends Case<TokenStream> {
        // Only for specification
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static abstract class PrimitiveCase<T> implements TokenStreamCase {
        private final Class<? extends Token<T>> type;
        private final Case<T> value;

        public PrimitiveCase(Class<?> type, Object object) {
            this.type = (Class<? extends Token<T>>) type; // TODO -- fix this cast
            this.value = fromObject(object);
        }

        @Override
        public Optional<MatchResult> unapply(TokenStream tokenStream) {
            final TokenStream secondary = tokenStream.secondary();

            final Token token;
            try {
                token = secondary.nextToken();
            } catch (IOException | UnexpectedCharException e) {
                return Optional.empty();
            }

            final Optional<MatchResult> resultOptional = this.unapplyToken(token);

            if (!resultOptional.isPresent()) {
                return Optional.empty();
            }

            tokenStream.commit(secondary);

            return matchFully(tokenStream, Optional.ofNullable(new MatchResult(token).with(resultOptional.get())));
        }

        protected Optional<MatchResult> unapplyToken(Token token) {
            if (this.type.isAssignableFrom(token.getClass())) {
                final Token<T> castToken = type.cast(token);
                final Optional<MatchResult> unapply = this.value.unapply(castToken.value());
                if (unapply.isPresent()) {
                    return Optional.ofNullable(new MatchResult(token).with(unapply.get()));
                }
            }

            return Optional.empty();
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
    private static class GenericCase<T> extends PrimitiveCase<T> {
        private final String kind;

        public GenericCase(String kind, Object object) {
            super(Token.GenericToken.class, object);
            this.kind = kind;
        }

        @Override
        protected Optional<MatchResult> unapplyToken(Token token) {
            if (Token.GenericToken.class.isAssignableFrom(token.getClass()) &&
                    Token.GenericToken.class.cast(token).kind().equals(this.kind)) {
                return super.unapplyToken(token);
            }

            return Optional.empty();
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
        private final Optional<Lexer> lexer;

        public ReentrantParser(Matcher<TokenStream, R> matcher) {
            super(matcher);
            this.lexer = Optional.empty();
        }

        public ReentrantParser(Matcher<TokenStream, R> matcher, Lexer lexer) {
            super(matcher);
            this.lexer = Optional.ofNullable(lexer);
        }

        @Override
        public Optional<MatchResult> unapply(TokenStream stream) {
            if (!lexer.isPresent()) {
                return super.unapply(stream);
            } else {
                final Lexer lexer = stream.setLexer(this.lexer.get());
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
        public Optional<MatchResult> unapply(TokenStream tokenStream) {
            final MatchResult result = new MatchResult(null);
            final List<Object> values = new ArrayList<>();
            final TokenStream secondary = tokenStream.secondary();

            for (Case<?> aCase : this.cases) {
                final Optional<MatchResult> unapply;

                if (isTokenStreamCase(aCase)) {
                    final Case<TokenStream> streamCase = (Case<TokenStream>) aCase;
                    unapply = streamCase.unapply(secondary);
                } else {
                    try {
                        final Case<Token> tokenCase = (Case<Token>) aCase;
                        unapply = tokenCase.unapply(secondary.nextToken());
                    } catch (IOException | UnexpectedCharException e) {
                        return Optional.empty();
                    }
                }

                if (!unapply.isPresent()) {
                    return unapply;
                } else {
                    values.add(unapply.get().matchedObject());
                    result.with(unapply.get());
                }
            }

            tokenStream.commit(secondary);

            return matchFully(tokenStream, Optional.ofNullable(new MatchResult(values).with(result)));
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
        public Optional<MatchResult> unapply(TokenStream tokenStream) {
            for (Case<?> aCase : this.cases) {
                final TokenStream secondary = tokenStream.secondary();
                final Optional<MatchResult> unapply;

                if (isTokenStreamCase(aCase)) {
                    final Case<TokenStream> streamCase = (Case<TokenStream>) aCase;
                    unapply = streamCase.unapply(secondary);
                } else {
                    try {
                        final Case<Token> tokenCase = (Case<Token>) aCase;
                        unapply = tokenCase.unapply(secondary.nextToken());
                    } catch (IOException | UnexpectedCharException e) {
                        return Optional.empty();
                    }
                }

                if (unapply.isPresent()) {
                    tokenStream.commit(secondary);
                    return matchFully(tokenStream, unapply);
                }
            }

            return Optional.empty();
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
        public Optional<MatchResult> unapply(TokenStream tokenStream) {
            final TokenStream secondary = tokenStream.secondary();
            final Optional<MatchResult> unapply = aCase.unapply(secondary);

            final MatchResult result;

            if (unapply.isPresent()) {
                tokenStream.commit(secondary);
                result = new MatchResult(Optional.ofNullable(unapply.get().matchedObject()));
                final List<Object> bindings = unapply.get().bindings();
                for (Object o : bindings) {
                    result.with(new MatchResult(Optional.ofNullable(o), null));
                }
            } else {
                // Simulate in order to collect variables
                result = new MatchResult(Optional.empty());
                final List<Class> classes = aCase.variableTypes();
                for (Class ignore : classes) {
                    result.with(new MatchResult(Optional.empty(), null));
                }
            }

            return matchFully(tokenStream, Optional.ofNullable(result));
        }

        @Override
        public List<Class> variableTypes() {
            return aCase.variableTypes();
        }
    }
}
