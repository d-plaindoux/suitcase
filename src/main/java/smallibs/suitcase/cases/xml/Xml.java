/*
 * Copyright (C)2013 D. Plaindoux.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; see the file COPYING.  If not, write to
 * the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package smallibs.suitcase.cases.xml;

import smallibs.suitcase.cases.genlex.JavaLexer;
import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.TokenRecognizer;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.match.Matcher;
import smallibs.suitcase.utils.Function2;

import static smallibs.suitcase.cases.core.Cases.var;
import static smallibs.suitcase.cases.genlex.Parser.Alt;
import static smallibs.suitcase.cases.genlex.Parser.Ident;
import static smallibs.suitcase.cases.genlex.Parser.Kwd;
import static smallibs.suitcase.cases.genlex.Parser.Opt;
import static smallibs.suitcase.cases.genlex.Parser.Seq;
import static smallibs.suitcase.cases.genlex.Parser.String;
import static smallibs.suitcase.cases.genlex.Parser.parser;
import static smallibs.suitcase.cases.xml.XmlGenLex.CData;
import static smallibs.suitcase.cases.xml.XmlGenLex.CDataRecognizer;

public final class Xml {

    // -----------------------------------------------------------------------------------------------------------------
    // JSON lexer definition
    // -----------------------------------------------------------------------------------------------------------------

    static private final Lexer elementLexer;

    static {
        elementLexer = new Lexer();
        elementLexer.skip("\\s+");
        elementLexer.keywords("<", ">", "</", "/>", "=");
        elementLexer.recognizers(TokenRecognizer.String(), TokenRecognizer.QuotedString(), JavaLexer.IDENT);
    }

    static private final Lexer elementsLexer;

    static {
        elementsLexer = new Lexer();
        elementsLexer.keywords("<", "</");
        elementsLexer.recognizers(CDataRecognizer("[^<]+"));
    }

    public static TokenStream stream(CharSequence sequence) {
        return elementLexer.parse(sequence);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Xml validation parser
    // -----------------------------------------------------------------------------------------------------------------

    static private final Matcher<TokenStream, Boolean> validator;

    static {
        validator = validator();
    }

    public static Boolean validate(TokenStream stream) {
        return validator.match(stream);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // XML validator - v0.1
    // -----------------------------------------------------------------------------------------------------------------

    public static Matcher<TokenStream, Boolean> validator() {
        final Matcher<TokenStream, Boolean> element, elements, attributes;

        element = parser(Matcher.<TokenStream, Boolean>create(), elementLexer);
        elements = parser(Matcher.<TokenStream, Boolean>create(), elementsLexer);
        attributes = parser(Matcher.<TokenStream, Boolean>create(), elementLexer);

        element.caseOf(Seq(Kwd("<"), Ident(var), attributes, Kwd("/>"))).then.value(true);
        element.caseOf(Seq(Kwd("<"), Ident(var), attributes, Kwd(">"), elements, Kwd("</"), Ident(var), Kwd(">"))).
                when(new Function2<String, String, Boolean>() {
                    @Override
                    public Boolean apply(String o1, String o2) throws Exception {
                        return o1.equals(o2);
                    }
                }).then.value(true);
        elements.caseOf(Opt(Seq(Alt(CData, element), elements))).then.value(true);

        attributes.caseOf(Opt(Seq(Ident, Kwd("="), String, attributes))).then.value(true);

        return element;
    }
}
