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
import smallibs.suitcase.cases.genlex.Token;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.match.Matcher;
import smallibs.suitcase.utils.Function;
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
import static smallibs.suitcase.cases.lang.Strings.Regex;

public final class JSon {

    public interface Match<R> {
        R match(TokenStream stream);
    }

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

    static private final Match<Boolean> validator;

    static {
        validator = JSon.with(new JSonValidator());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // JSON and handler
    // -----------------------------------------------------------------------------------------------------------------

    public static Boolean validate(TokenStream stream) {
        return validator.match(stream);
    }

    public static <R, MS, M, VS, V> Match<R> with(final JSonHandler<R, MS, M, VS, V> handler) {
        final Matcher<TokenStream, R> main, object, array;
        final Matcher<TokenStream, Object> members, otherMembers, member, values, otherValues, value;

        main = parser(Matcher.<TokenStream, R>create());
        object = parser(Matcher.<TokenStream, R>create());
        array = parser(Matcher.<TokenStream, R>create());

        members = parser(Matcher.<TokenStream, Object>create());
        otherMembers = parser(Matcher.<TokenStream, Object>create());
        member = parser(Matcher.<TokenStream, Object>create());
        values = parser(Matcher.<TokenStream, Object>create());
        otherValues = parser(Matcher.<TokenStream, Object>create());
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

        members.caseOf(Seq(var.of(member), var.of(Opt(otherMembers)))).then.function(new Function2<M, Option<MS>, MS>() {
            @Override
            public MS apply(M o1, Option<MS> o2) throws Exception {
                return handler.someMembers(o1, o2);
            }
        });
        otherMembers.caseOf(Seq(Kwd(","), var.of(members))).then.function(new Function<MS, MS>() {
            @Override
            public MS apply(MS o) throws Exception {
                return o;
            }
        });


        member.caseOf(Seq(var.of(String), Kwd(":"), var.of(value))).then.function(new Function2<Token.StringToken, V, M>() {
            @Override
            public M apply(Token.StringToken o1, V o2) throws Exception {
                return handler.aMember(o1.value(), o2);
            }
        });

        values.caseOf(Seq(var.of(value), var.of(Opt(otherValues)))).then.function(new Function2<V, Option<VS>, VS>() {
            @Override
            public VS apply(V o1, Option<VS> o2) throws Exception {
                return handler.someValues(o1, o2);
            }
        });
        otherValues.caseOf(Seq(Kwd(","), var.of(values))).then.function(new Function<VS, VS>() {
            @Override
            public VS apply(VS o) throws Exception {
                return o;
            }
        });

        value.caseOf(var.of(main)).then.function(new Function<R, V>() {
            @Override
            public V apply(R o) throws Exception {
                return handler.aValue(o);
            }
        });

        value.caseOf(var.of(Alt(String, Int, Float))).then.function(new Function<Token, V>() {
            @Override
            public V apply(Token o) throws Exception {
                final Object value = o.value();
                if (value instanceof String) {
                    return handler.aString((String) value);
                }
                if (value instanceof Integer) {
                    return handler.anInteger((Integer) value);
                }
                if (value instanceof Float) {
                    return handler.aFloat((Float) value);
                }

                throw new IllegalArgumentException();
            }
        });

        value.caseOf(var.of(Kwd(Regex("null|true|false")))).then.function(new Function<Token, Object>() {
            @Override
            public Object apply(Token o) throws Exception {
                final Object value = o.value();
                if (value.equals("null")) {
                    return handler.aNull();
                }
                if (value.equals("false")) {
                    return handler.aBoolean(false);
                }
                if (value.equals("true")) {
                    return handler.aBoolean(true);
                }

                throw new IllegalArgumentException();
            }
        });

        return new Match<R>() {
            @Override
            public R match(TokenStream stream) {
                return main.match(stream);
            }
        };
    }

}
