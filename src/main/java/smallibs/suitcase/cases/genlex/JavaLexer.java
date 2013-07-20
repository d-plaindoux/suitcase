package smallibs.suitcase.cases.genlex;

import static smallibs.suitcase.cases.genlex.TokenRecognizer.Hexa;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Ident;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Int;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.String;

public class JavaLexer extends Lexer {

    public JavaLexer(TokenRecognizer... recognizers) {
        super(recognizers);
        // Hexadecimal Number
        this.recognizers(Hexa());
        // Number
        this.recognizers(Int());
        // String
        this.recognizers(String());
        // Identifier
        this.recognizers(Ident("([\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*"));
        // Skip spaces
        this.skip("\\s+");
    }

}
