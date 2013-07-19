package smallibs.suitcase.cases.genlex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private final List<TokenRecognizer> recognizers;

    public Lexer(TokenRecognizer... recognizers) {
        this.recognizers = new ArrayList<>();
        this.recognizers(recognizers);
    }

    public Lexer recognizers(TokenRecognizer... tokens) {
        this.recognizers.addAll(Arrays.asList(tokens));
        return this;
    }

    public TokenStream parse(CharSequence sequence) {
        return new TokenStream(this, sequence);
    }

    List<TokenRecognizer> getRecognizers() {
        return recognizers;
    }

    public Lexer keywords(String... keywords) {
        for (String keyword : keywords) {
            this.recognizers.add(TokenRecognizer.Keyword(keyword));
        }
        return this;
    }
}
