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

package smallibs.suitcase.cases.genlex;

import static smallibs.suitcase.cases.genlex.TokenRecognizer.Hexa;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Ident;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.Int;
import static smallibs.suitcase.cases.genlex.TokenRecognizer.String;

public class JavaLexer extends Lexer {

    public static final TokenRecognizer IDENT = TokenRecognizer.Ident("([\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*");

    public JavaLexer() {
        super();
        // Hexadecimal Number
        this.recognizers(TokenRecognizer.Hexa());
        // Number
        this.recognizers(TokenRecognizer.Int());
        // String
        this.recognizers(TokenRecognizer.String());
        // Identifier
        this.recognizers(IDENT);
        // Skip spaces
        this.skip("\\s+");
    }

}
