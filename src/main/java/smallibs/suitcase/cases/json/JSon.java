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

package smallibs.suitcase.cases.json;

import smallibs.suitcase.cases.core.ReentrantMatcher;
import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.match.Matcher;

import static smallibs.suitcase.cases.genlex.Parser.Alt;
import static smallibs.suitcase.cases.genlex.Parser.Float;
import static smallibs.suitcase.cases.genlex.Parser.Int;
import static smallibs.suitcase.cases.genlex.Parser.Kwd;
import static smallibs.suitcase.cases.genlex.Parser.Opt;
import static smallibs.suitcase.cases.genlex.Parser.Seq;
import static smallibs.suitcase.cases.genlex.Parser.String;
import static smallibs.suitcase.cases.genlex.Parser.parser;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Float;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Int;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.QuotedString;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.String;
import static smallibs.suitcase.cases.lang.Strings.Regex;

public final class JSon {

    // -----------------------------------------------------------------------------------------------------------------
    // JSON lexer definition
    // -----------------------------------------------------------------------------------------------------------------

    static private final Lexer jsonLexer;

    static {
        jsonLexer = new Lexer();
        jsonLexer.skip("[ \n\r\f\t]+");
        jsonLexer.recognizers(Float());
        jsonLexer.recognizers(Int());
        jsonLexer.recognizers(String());
        jsonLexer.recognizers(QuotedString());
        jsonLexer.keywords("[", "]", "{", "}", ":", ",");
        jsonLexer.keywords("null", "true", "false");

    }

    public static TokenStream stream(CharSequence sequence) {
        return jsonLexer.parse(sequence);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // JSON parser definition
    // -----------------------------------------------------------------------------------------------------------------

    static private final ReentrantMatcher<TokenStream, Boolean> main, value, object, array, members, member, values;

    static {
        // Parser entries creation

        main = parser(Matcher.<TokenStream, Boolean>create());
        object = parser(Matcher.<TokenStream, Boolean>create());
        value = parser(Matcher.<TokenStream, Boolean>create());
        array = parser(Matcher.<TokenStream, Boolean>create());
        members = parser(Matcher.<TokenStream, Boolean>create());
        member = parser(Matcher.<TokenStream, Boolean>create());
        values = parser(Matcher.<TokenStream, Boolean>create());

        // Parse rules definition

        main.caseOf(Alt(object, array)).then.value(true);

        object.caseOf(Seq(Kwd("{"), Opt(members), Kwd("}"))).then.value(true);
        array.caseOf(Seq(Kwd("["), Opt(values), Kwd("]"))).then.value(true);

        members.caseOf(Seq(member, Opt(Kwd(","), members))).then.value(true);
        member.caseOf(Seq(String, Kwd(":"), value)).then.value(true);

        value.caseOf(Alt(main, String, Int, Float, Kwd(Regex("null|true|false")))).then.value(true);
        values.caseOf(Seq(value, Opt(Kwd(","), values))).then.value(true);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static Boolean match(TokenStream stream) {
        return main.match(stream);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {

    }
}
