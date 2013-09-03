package smallibs.suitcase.cases.xml;

import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.genlex.Parser;
import smallibs.suitcase.cases.genlex.Tokenizer;
import smallibs.suitcase.cases.genlex.TokenStream;

import static smallibs.suitcase.cases.core.Cases._;

public class XmlGenLex {

    public static final String TEXT = "Text";

    public static Tokenizer.GenericRecognizer TextTokenizer(String value) {
        return Tokenizer.Generic(TEXT, Tokenizer.pattern(value));
    }

    public static Case<TokenStream> Text = Text(_);

    public static Case<TokenStream> Text(Object aCase) {
        return Parser.A(TEXT, aCase);
    }
}
