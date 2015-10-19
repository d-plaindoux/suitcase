/*
 * Copyright (C)2015 D. Plaindoux.
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

package org.smallibs.suitcase.cases.xml;

import org.smallibs.suitcase.cases.genlex.JavaLexer;
import org.smallibs.suitcase.cases.genlex.Lexer;
import org.smallibs.suitcase.cases.genlex.TokenStream;
import org.smallibs.suitcase.cases.genlex.Tokenizer;
import org.smallibs.suitcase.match.Matcher;
import org.smallibs.suitcase.utils.Function;
import java.util.Optional;

import static org.smallibs.suitcase.cases.core.Cases.var;
import static org.smallibs.suitcase.cases.genlex.Parser.Alt;
import static org.smallibs.suitcase.cases.genlex.Parser.Ident;
import static org.smallibs.suitcase.cases.genlex.Parser.Kwd;
import static org.smallibs.suitcase.cases.genlex.Parser.Opt;
import static org.smallibs.suitcase.cases.genlex.Parser.Seq;
import static org.smallibs.suitcase.cases.genlex.Parser.String;
import static org.smallibs.suitcase.cases.genlex.Parser.parser;
import static org.smallibs.suitcase.cases.xml.XmlGenLex.Text;
import static org.smallibs.suitcase.cases.xml.XmlGenLex.TextTokenizer;

public final class Xml {

    // -----------------------------------------------------------------------------------------------------------------
    // JSON lexer definition
    // -----------------------------------------------------------------------------------------------------------------

    static private final Lexer elementLexer;
    static private final Lexer commentLexer;
    static private final Lexer textLexer;
    static private final Lexer cdataLexer;
    static private final Matcher<TokenStream, Boolean> validator;

    static {
        elementLexer = new Lexer();
        elementLexer.skip("\\s+");
        elementLexer.tokenizers(Tokenizer.Kwd("</"), Tokenizer.Kwd("/>"), Tokenizer.Kwd("<"), Tokenizer.Kwd(">"), Tokenizer.Kwd("="));
        elementLexer.tokenizers(Tokenizer.String(), Tokenizer.QuotedString(), JavaLexer.IDENT);
    }

    static {
        commentLexer = new Lexer();
        commentLexer.tokenizers(Tokenizer.Kwd("<!--"), Tokenizer.Kwd("-->"));
        commentLexer.tokenizers(TextTokenizer(".*?(?=--)"));
    }

    static {
        textLexer = new Lexer();
        textLexer.tokenizers(TextTokenizer("[^<]+"));
    }

    static {
        cdataLexer = new Lexer();
        cdataLexer.tokenizers(Tokenizer.Kwd("<![CDATA["), Tokenizer.Kwd("]]>"));
        cdataLexer.tokenizers(TextTokenizer(".*?(?=]]>)"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Xml validation parser
    // -----------------------------------------------------------------------------------------------------------------

    static {
        validator = handleWith(new XmlValidator());
    }

    public static TokenStream stream(CharSequence sequence) {
        return elementLexer.parse(sequence);
    }

    public static Boolean validate(TokenStream stream) {
        return validator.match(stream);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // XML validator - v0.1
    // -----------------------------------------------------------------------------------------------------------------

    public static <ES, E, AS, A> Matcher<TokenStream, E> handleWith(final XmlHandler<ES, E, AS, A> handler) {
        final Matcher<TokenStream, E> main;
        final Matcher<TokenStream, ES> elements;
        final Matcher<TokenStream, E> element;
        final Matcher<TokenStream, E> text;
        final Matcher<TokenStream, E> cdata;
        final Matcher<TokenStream, Boolean> comments;
        final Matcher<TokenStream, E> tag;
        final Matcher<TokenStream, AS> attributes;

        main = parser(Matcher.<TokenStream, E>create(), elementLexer);
        elements = parser(Matcher.<TokenStream, ES>create(), elementLexer);
        element = parser(Matcher.<TokenStream, E>create(), elementLexer);
        text = parser(Matcher.<TokenStream, E>create(), textLexer);
        cdata = parser(Matcher.<TokenStream, E>create(), cdataLexer);
        comments = parser(Matcher.<TokenStream, Boolean>create(), commentLexer);
        tag = parser(Matcher.<TokenStream, E>create(), elementLexer);
        attributes = parser(Matcher.<TokenStream, AS>create(), elementLexer);

        main.caseOf(Seq(Opt(comments), var.of(element), Opt(comments))).then(new Function<E, E>() {
            @Override
            public E apply(E element) throws Exception {
                return element;
            }
        });

        elements.caseOf(Seq(var.of(element), Opt(comments), Opt(var.of(elements)))).then(handler::someElements);
        text.caseOf(Text(var)).then(handler::aText);

        element.caseOf(var.of(Alt(cdata, text, tag))).then((E i) -> i);

        cdata.caseOf(Seq(Kwd("<![CDATA["), Text(var), Kwd("]]>"))).then(handler::aText);

        comments.caseOf(Seq(Kwd("<!--"), Text, Kwd("-->"), Opt(comments))).then(true);

        tag.caseOf(Seq(Kwd("<"), Ident(var), Opt(var.of(attributes)), Kwd("/>"))).
                then((String name, Optional<AS> atts) -> handler.anElement(name, atts, Optional.<ES>empty()));

        tag.caseOf(Seq(Kwd("<"), Ident(var), Opt(var.of(attributes)), Kwd(">"), Opt(comments), Opt(var.of(elements)), Kwd("</"), Ident(var), Kwd(">"))).
                when((String sname, Optional<AS> atts, Optional<ES> content, String ename) -> sname.equals(ename)).
                then((String sname, Optional<AS> atts, Optional<ES> content, String ename) -> handler.anElement(sname, atts, content));

        attributes.caseOf(Seq(Ident(var), Kwd("="), String(var), Opt(var.of(attributes)))).
                then((String name, String value, Optional<AS> attribute) -> handler.someAttributes(handler.anAttribute(name, value), attribute));

        return main;
    }
}
