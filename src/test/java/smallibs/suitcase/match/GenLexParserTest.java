package smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import smallibs.suitcase.cases.core.Cases;
import smallibs.suitcase.cases.genlex.JavaLexer;
import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.Parser;
import smallibs.suitcase.cases.genlex.Token;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.cases.genlex.Tokenizer;
import smallibs.suitcase.utils.Function;

import java.util.List;

import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.core.Cases.var;
import static smallibs.suitcase.cases.genlex.Parser.Alt;
import static smallibs.suitcase.cases.genlex.Parser.Ident;
import static smallibs.suitcase.cases.genlex.Parser.Int;
import static smallibs.suitcase.cases.genlex.Parser.Kwd;
import static smallibs.suitcase.cases.genlex.Parser.Opt;
import static smallibs.suitcase.cases.genlex.Parser.Seq;

public class GenLexParserTest {

    private Lexer givenALexer() {
        return new JavaLexer().recognizers(Tokenizer.Kwd("("), Tokenizer.Kwd(")"), Tokenizer.Kwd("{"), Tokenizer.Kwd("}"));
    }

    @Test
    public void shouldParseIdent() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Ident).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("Hello");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseInt() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Int).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("1942");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseHex() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Int).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("0x12");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseAnyKeyword() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Kwd(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("(");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseAKeyword() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Kwd("(")).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("(");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseIdentInParenthesis() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Kwd("("), Ident, Kwd(")"))).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("(Hello)");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseWithTwoCases() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Kwd("("))).then.value(true);
        matcher.caseOf(Seq(Kwd("{"))).then.value(true);
        matcher.caseOf(Cases._).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("{");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseWithAny() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(_).then.value(true);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("{");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseWithVariable() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(var).then.function(new Function<TokenStream, Boolean>() {
            @Override
            public Boolean apply(TokenStream tokenStream) throws Exception {
                final Token token = tokenStream.nextToken();
                return token instanceof Token.IdentToken && token.value().equals("Hello");
            }
        });

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("Hello");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseIdentWithVariable() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(Ident)).then.function(new Function<Token, Boolean>() {
            @Override
            public Boolean apply(Token token) throws Exception {
                return token instanceof Token.IdentToken && token.value().equals("Hello");
            }
        });

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("Hello");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseKeywordWithVariable() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(Kwd(_))).then.function(new Function<Token, Boolean>() {
            @Override
            public Boolean apply(Token token) throws Exception {
                return token instanceof Token.KeywordToken && token.value().equals("{");
            }
        });

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("{");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseSequenceWithVariable() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(Seq(_, _))).then.function(new Function<List<Object>, Boolean>() {
            @Override
            public Boolean apply(List<Object> tokens) throws Exception {
                final Token token1 = (Token) tokens.get(0);
                final Token token2 = (Token) tokens.get(1);
                return token1 instanceof Token.KeywordToken && token1.value().equals("{") &&
                        token2 instanceof Token.IdentToken && token2.value().equals("Hello");
            }
        });

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("{Hello");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseSequenceWithReentrantParser() {
        final Matcher<TokenStream, Boolean> parser = Matcher.create();
        final Matcher<TokenStream, Boolean> matcher = Parser.parser(parser);

        matcher.caseOf(Seq(Kwd(_), var.of(matcher))).then.function(new Function<Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean) throws Exception {
                return aBoolean;
            }
        });
        matcher.caseOf(Ident).then.value(true);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("{Hello");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseSequenceWithAlt() {
        final Matcher<TokenStream, Boolean> parser = Matcher.create();
        final Matcher<TokenStream, Boolean> matcher = Parser.parser(parser);

        matcher.caseOf(Alt(Kwd(_), Ident)).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("{");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseSequenceWithAlt2() {
        final Matcher<TokenStream, Boolean> parser = Matcher.create();
        final Matcher<TokenStream, Boolean> matcher = Parser.parser(parser);

        matcher.caseOf(Alt(Kwd(_), Ident)).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("Hello");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseSequenceWithOpt() {
        final Matcher<TokenStream, Boolean> parser = Matcher.create();
        final Matcher<TokenStream, Boolean> matcher = Parser.parser(parser);

        matcher.caseOf(Opt(Kwd(_))).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldParseSequenceWithOptAndIdent() {
        final Matcher<TokenStream, Boolean> parser = Matcher.create();
        final Matcher<TokenStream, Boolean> matcher = Parser.parser(parser);

        matcher.caseOf(Seq(Opt(Kwd(_)), Ident)).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("Hello");

        TestCase.assertTrue(matcher.match(stream));
    }

    @Test
    public void shouldNotParsePartialSequence() {
        final Matcher<TokenStream, Boolean> parser = Matcher.create();
        final Matcher<TokenStream, Boolean> matcher = Parser.parser(parser);

        matcher.caseOf(Ident).then.value(true);
        matcher.caseOf(_).then.value(false);

        final Lexer lexer = givenALexer();
        final TokenStream stream = lexer.parse("Hello World");

        TestCase.assertFalse(matcher.match(stream));
    }
}
