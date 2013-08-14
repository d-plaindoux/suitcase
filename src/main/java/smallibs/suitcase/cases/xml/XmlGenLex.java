package smallibs.suitcase.cases.xml;

import smallibs.suitcase.annotations.CaseType;
import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.genlex.Parser;
import smallibs.suitcase.cases.genlex.Token;
import smallibs.suitcase.cases.genlex.TokenRecognizer;
import smallibs.suitcase.cases.genlex.TokenStream;

import static smallibs.suitcase.cases.core.Cases._;

public class XmlGenLex {

    public static Token<String> TextToken(String value) {
        return new TextToken(value);
    }

    private static class TextToken extends Token<String> {
        private final String value;

        public TextToken(String value) {
            super();
            this.value = value;
        }

        @Override
        public String value() {
            return this.value;
        }

        @Override
        public int length() {
            return this.value.length();
        }
    }

    public static TextRecognizer TextRecognizer(String value) {
        return new TextRecognizer(value);
    }


    private static class TextRecognizer extends TokenRecognizer.PatternRecognizer {
        public TextRecognizer(String value) {
            super(value);
        }

        @Override
        protected Token<?> matched(String string) {
            return TextToken(string);
        }
    }

    public static Case<TokenStream> Text = Text(_);

    public static Case<TokenStream> Text(Object aCase) {
        return new TextCase(aCase);
    }

    @CaseType(TokenStream.class)
    private static class TextCase extends Parser.PrimitiveCase<String> {
        public TextCase(Object object) {
            super(TextToken.class, object);
        }
    }
}
