package smallibs.suitcase.cases.xml;

import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.genlex.Parser;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.cases.genlex.Tokenizer;

import static smallibs.suitcase.cases.core.Cases.__;

public class XmlGenLex {

    public static final String TEXT = "Text";
    public static Case<TokenStream> Text = Text(__);

    public static Tokenizer.GenericRecognizer TextTokenizer(String value) {
        return Tokenizer.Generic(TEXT, Tokenizer.pattern(value));
    }

    public static Case<TokenStream> Text(Object aCase) {
        return Parser.A(TEXT, aCase);
    }
}
