package smallibs.suitcase.cases.xml;

import smallibs.suitcase.annotations.CaseType;
import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.genlex.Parser;
import smallibs.suitcase.cases.genlex.Token;
import smallibs.suitcase.cases.genlex.TokenRecognizer;
import smallibs.suitcase.cases.genlex.TokenStream;

import static smallibs.suitcase.cases.core.Cases._;

public class XmlGenLex {

    public static Token<String> CDataToken(String value) {
        return new CDataToken(value);
    }

    private static class CDataToken extends Token<String> {
        private final String value;

        public CDataToken(String value) {
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

    public static CDataRecognizer CDataRecognizer(String value) {
        return new CDataRecognizer(value);
    }


    private static class CDataRecognizer extends TokenRecognizer.PatternRecognizer {
        public CDataRecognizer(String value) {
            super(value);
        }

        @Override
        protected Token<?> matched(String string) {
            return CDataToken(string);
        }
    }

    public static Case<TokenStream> CData = CData(_);

    public static Case<TokenStream> CData(Object aCase) {
        return new CDataCase(aCase);
    }

    @CaseType(TokenStream.class)
    private static class CDataCase extends Parser.PrimitiveCase<String> {
        public CDataCase(Object object) {
            super(CDataToken.class, object);
        }
    }
}
