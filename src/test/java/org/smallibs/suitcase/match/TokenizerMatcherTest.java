package org.smallibs.suitcase.match;

import org.junit.Test;
import org.smallibs.suitcase.cases.genlex.Token;
import org.smallibs.suitcase.cases.genlex.Tokenizer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smallibs.suitcase.cases.core.Cases.Var;

public class TokenizerMatcherTest {
    @Test
    public void shouldMatchAnInteger() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tokenizer.Int()).then(true);

        assertThat(matcher.match("123")).isTrue();
    }

    @Test
    public void shouldMatchAndRetrieveASnInteger() throws Exception {
        Matcher<CharSequence, Integer> matcher = Matcher.create();

        matcher.caseOf(Var(Tokenizer.Int())).then(Token::value);

        assertThat(matcher.match("123")).isEqualTo(123);
    }

    @Test
    public void shouldMatchAString() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tokenizer.String()).then(true);

        assertThat(matcher.match("\"123\"")).isTrue();
    }

    @Test
    public void shouldMatchAQuotedString() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tokenizer.QuotedString()).then(true);

        assertThat(matcher.match("'123'")).isTrue();
    }

    @Test
    public void shouldMatchAKeyword() throws Exception {
        Matcher<CharSequence, Boolean> matcher = Matcher.create();

        matcher.caseOf(Var(Tokenizer.Kwd("+"))).then(true);

        assertThat(matcher.match("+")).isTrue();
    }
}
