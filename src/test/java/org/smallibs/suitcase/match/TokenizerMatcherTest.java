package org.smallibs.suitcase.match;

import org.junit.Test;
import org.smallibs.suitcase.cases.genlex.Token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.cases.genlex.Tokenizer.Hexa;
import static org.smallibs.suitcase.cases.genlex.Tokenizer.Ident;
import static org.smallibs.suitcase.cases.genlex.Tokenizer.Int;
import static org.smallibs.suitcase.cases.genlex.Tokenizer.Kwd;
import static org.smallibs.suitcase.cases.genlex.Tokenizer.QuotedString;
import static org.smallibs.suitcase.cases.genlex.Tokenizer.String;

public class TokenizerMatcherTest {
    @Test
    public void shouldMatchAnInteger() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(Int()).then(true);

        assertThat(matcher.match("123")).isTrue();
    }

    @Test
    public void shouldMatchAnHexadecimal() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(Hexa()).then(true);

        assertThat(matcher.match("0x123")).isTrue();
    }

    @Test
    public void shouldMatchAndRetrieveAnInteger() throws Exception {
        Matcher<CharSequence, Integer> matcher = Matcher.create();

        matcher.caseOf(Var(Int())).then(Token::value);

        assertThat(matcher.match("123")).isEqualTo(123);
    }

    @Test
    public void shouldMatchAndRetrieveAnHexadecimal() throws Exception {
        Matcher<CharSequence, Integer> matcher = Matcher.create();

        matcher.caseOf(Var(Hexa())).then(Token::value);

        assertThat(matcher.match("0x123")).isEqualTo(0x123);
    }

    @Test
    public void shouldMatchAString() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(String()).then(true);

        assertThat(matcher.match("\"123\"")).isTrue();
    }

    @Test
    public void shouldMatchAndRetrieveAString() throws Exception {
        Matcher<CharSequence, String> matcher = Matcher.create();

        matcher.caseOf(Var(String())).then(Token::value);

        assertThat(matcher.match("\"123\"")).isEqualTo("123");
    }

    @Test
    public void shouldMatchAQuotedString() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(QuotedString()).then(true);

        assertThat(matcher.match("'123'")).isTrue();
    }

    @Test
    public void shouldMatchAndRetrieveAQuotedString() throws Exception {
        Matcher<CharSequence, String> matcher = Matcher.create();

        matcher.caseOf(Var(QuotedString())).then(Token::value);

        assertThat(matcher.match("'123'")).isEqualTo("123");
    }

    @Test
    public void shouldMatchAKeyword() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(Var(Kwd("+"))).then(true);

        assertThat(matcher.match("+")).isTrue();
    }

    @Test
    public void shouldMatchAnIdent() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(Var(Ident("new"))).then(true);

        assertThat(matcher.match("new")).isTrue();
    }
}
