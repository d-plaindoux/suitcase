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

import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.match.Matcher;
import smallibs.suitcase.utils.Function;
import smallibs.suitcase.utils.Function0;
import smallibs.suitcase.utils.Function2;
import smallibs.suitcase.utils.Option;

import static smallibs.suitcase.cases.core.Cases.var;
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

public final class JSon {

    // -----------------------------------------------------------------------------------------------------------------
    // JSON lexer definition
    // -----------------------------------------------------------------------------------------------------------------

    static private final Lexer jsonLexer;

    static {
        jsonLexer = new Lexer();
        jsonLexer.skip("\\s+");
        jsonLexer.keywords("[", "]", "{", "}", ":", ",");
        jsonLexer.keywords("null", "true", "false");
        jsonLexer.recognizers(Float(), Int(), String(), QuotedString());
    }

    public static TokenStream stream(CharSequence sequence) {
        return jsonLexer.parse(sequence);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // JSON validation parser
    // -----------------------------------------------------------------------------------------------------------------

    static private final Matcher<TokenStream, Boolean> validator;

    static {
        validator = JSon.withHandler(new JSonValidator());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // JSON and handler
    // -----------------------------------------------------------------------------------------------------------------

    public static Boolean validate(TokenStream stream) {
        return validator.match(stream);
    }

    public static <R, MS, M, VS, V> Matcher<TokenStream, R> withHandler(final JSonHandler<R, MS, M, VS, V> handler) {
        final Matcher<TokenStream, R> main, object, array;
        final Matcher<TokenStream, Object> members, member, values, value;

        main = parser(Matcher.<TokenStream, R>create());
        object = parser(Matcher.<TokenStream, R>create());
        array = parser(Matcher.<TokenStream, R>create());
        members = parser(Matcher.<TokenStream, Object>create());
        member = parser(Matcher.<TokenStream, Object>create());
        values = parser(Matcher.<TokenStream, Object>create());
        value = parser(Matcher.<TokenStream, Object>create());

        // Parse rules definition

        main.caseOf(var.of(Alt(object, array))).then.function(new Function<R, R>() {
            @Override
            public R apply(R r) throws Exception {
                return r;
            }
        });

        object.caseOf(Seq(Kwd("{"), var.of(Opt(members)), Kwd("}"))).then.function(new Function<Option<MS>, R>() {
            @Override
            public R apply(Option<MS> o) throws Exception {
                return handler.anObject(o);
            }
        });

        array.caseOf(Seq(Kwd("["), var.of(Opt(values)), Kwd("]"))).then.function(new Function<Option<VS>, R>() {
            @Override
            public R apply(Option<VS> o) throws Exception {
                return handler.anArray(o);
            }
        });

        members.caseOf(Seq(var.of(member), Opt(Seq(Kwd(","), var.of(members))))).then.function(new Function2<M, Option<MS>, MS>() {
            @Override
            public MS apply(M o1, Option<MS> o2) throws Exception {
                return handler.someMembers(o1, o2);
            }
        });

        member.caseOf(Seq(String(var), Kwd(":"), var.of(value))).then.function(new Function2<String, V, M>() {
            @Override
            public M apply(String o1, V o2) throws Exception {
                return handler.aMember(o1, o2);
            }
        });

        values.caseOf(Seq(var.of(value), Opt(Seq(Kwd(","), var.of(values))))).then.function(new Function2<V, Option<VS>, VS>() {
            @Override
            public VS apply(V o1, Option<VS> o2) throws Exception {
                return handler.someValues(o1, o2);
            }
        });

        value.caseOf(var.of(main)).then.function(new Function<R, V>() {
            @Override
            public V apply(R o) throws Exception {
                return handler.aValue(o);
            }
        });
        value.caseOf(String(var)).then.function(new Function<String, V>() {
            @Override
            public V apply(String o) throws Exception {
                return handler.aString(o);
            }
        });
        value.caseOf(Int(var)).then.function(new Function<Integer, V>() {
            @Override
            public V apply(Integer o) throws Exception {
                return handler.anInteger(o);
            }
        });
        value.caseOf(Float(var)).then.function(new Function<Float, V>() {
            @Override
            public V apply(Float o) throws Exception {
                return handler.aFloat(o);
            }
        });
        value.caseOf(Kwd("null")).then.function(new Function0<Object>() {
            @Override
            public Object apply() throws Exception {
                return handler.aNull();
            }
        });
        value.caseOf(Kwd("true")).then.function(new Function0<Object>() {
            @Override
            public Object apply() throws Exception {
                return handler.aBoolean(true);
            }
        });
        value.caseOf(Kwd("false")).then.function(new Function0<Object>() {
            @Override
            public Object apply() throws Exception {
                return handler.aBoolean(false);
            }
        });

        return main;
    }

}
