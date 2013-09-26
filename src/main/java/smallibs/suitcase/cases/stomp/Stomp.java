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

package smallibs.suitcase.cases.stomp;

import smallibs.suitcase.cases.genlex.Lexer;
import smallibs.suitcase.cases.genlex.TokenStream;
import smallibs.suitcase.cases.genlex.Tokenizer;
import smallibs.suitcase.match.Matcher;

import static smallibs.suitcase.cases.genlex.Parser.Alt;
import static smallibs.suitcase.cases.genlex.Parser.Ident;
import static smallibs.suitcase.cases.genlex.Parser.Kwd;
import static smallibs.suitcase.cases.genlex.Parser.Opt;
import static smallibs.suitcase.cases.genlex.Parser.Seq;
import static smallibs.suitcase.cases.genlex.Parser.parser;
import static smallibs.suitcase.cases.lang.Strings.Regex;

public final class Stomp {

    private static final String NULL = "\\u0000";

    public static final String LF = "\\u000A";
    public static final String CR = "\\u000D";
    private static final String EOL = LF + "?" + CR;
    private static final String CONTENT = "[^" + NULL + "]+";
    private static final String CLIENT_COMMAND = "SEND|SUBSCRIBE|UNSUBSCRIBE|BEGIN|COMMIT|ABORT|ACK|NACK|DISCONNECT|CONNECT|STOMP";
    private static final String SERVER_COMMAND = "CONNECTED|MESSAGE|RECEIPT|ERROR";

    static private final Lexer stompLexer;
    private static final Lexer stompHeader;
    static private final Lexer stompContent;

    public static final String header_name_value = "[^" + LF + CR + ":]+";

    static {
        stompLexer = new Lexer();
        stompLexer.tokenizers(Tokenizer.Ident(EOL));
        stompLexer.tokenizers(Tokenizer.Ident(CLIENT_COMMAND));
        stompLexer.tokenizers(Tokenizer.Ident(SERVER_COMMAND));

        stompHeader = new Lexer();
        stompHeader.tokenizers(Tokenizer.Ident(header_name_value), Tokenizer.Ident(EOL), Tokenizer.Kwd(":"));

        stompContent = new Lexer();
        stompContent.tokenizers(Tokenizer.Ident(CONTENT));
    }

    public static TokenStream stream(CharSequence sequence) {
        return stompLexer.parse(sequence);
    }

    public static boolean validate(TokenStream stream) {
        final Matcher<TokenStream, Boolean> main, frame, command, header, headers, content;

        main = parser(new Matcher<TokenStream, Boolean>(), stompLexer);
        frame = parser(new Matcher<TokenStream, Boolean>(), stompLexer);
        command = parser(new Matcher<TokenStream, Boolean>(), stompLexer);
        headers = parser(new Matcher<TokenStream, Boolean>(), stompHeader);
        header = parser(new Matcher<TokenStream, Boolean>(), stompHeader);
        content = parser(new Matcher<TokenStream, Boolean>(), stompContent);

        main.caseOf(Seq(frame, Opt(main))).then.value(true);
        main.caseOf(Seq(Ident(Regex(EOL)), Opt(main))).then.value(true);
        frame.caseOf(Seq(command, Opt(header), Ident(Regex(EOL)), Opt(content), Ident(Regex(NULL)))).then.value(true);
        command.caseOf(Alt(Ident)).then.value(true);
        headers.caseOf(Seq(header, Ident(Regex(EOL)), Opt(headers))).then.value(true);
        header.caseOf(Seq(Ident(Regex(header_name_value)), Kwd(":"), Opt(Ident(Regex(header_name_value))))).then.value(true);
        content.caseOf(Ident).then.value(true);

        return main.match(stream);
    }

}
