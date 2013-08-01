package smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import smallibs.suitcase.cases.core.Cases;
import smallibs.suitcase.cases.genlex.JavaLexer;
import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.TokenStream;

import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.genlex.Parser.Ident;
import static smallibs.suitcase.cases.genlex.Parser.Int;
import static smallibs.suitcase.cases.genlex.Parser.Kwd;
import static smallibs.suitcase.cases.genlex.Parser.Seq;

public class GenLexParserTest {

    private Lexer givenALexer() {
        return new JavaLexer().keywords("(", ")", "{", "}");
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
        matcher.caseOf(Cases._).then.value(false);

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

}
