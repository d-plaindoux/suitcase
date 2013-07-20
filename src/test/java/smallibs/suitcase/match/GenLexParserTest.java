package smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import smallibs.suitcase.cases.core.Cases;
import smallibs.suitcase.cases.genlex.JavaLexer;
import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.TokenStream;

import static smallibs.suitcase.cases.genlex.Parser.Ident;
import static smallibs.suitcase.cases.genlex.Parser.Kwd;
import static smallibs.suitcase.cases.genlex.Parser.Seq;

public class GenLexParserTest {

    @Test
    public void shouldParseIdent() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Ident).then.value(true);
        matcher.caseOf(Cases._).then.value(false);

        final Lexer lexer = new JavaLexer();

        TestCase.assertTrue(matcher.match(lexer.parse("Hello")));
    }

    /*
    @Test
    public void shouldParseIdentInParenthesis() {
        final Matcher<TokenStream, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Kwd("("), Ident, Kwd(")"))).then.value(true);
        matcher.caseOf(Cases._).then.value(false);

        final Lexer lexer = new JavaLexer().keywords("(", ")");

        TestCase.assertTrue(matcher.match(lexer.parse("( Hello )")));
    }
    */
}
