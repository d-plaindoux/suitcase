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

package org.smallibs.suitcase.pattern.utils;

import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.utils.Option;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class Strings {

    public static Case<String> Regex(String expression) {
        return new RegularExpression(Pattern.compile(expression));
    }

    private static class RegularExpression implements Case<String> {
        private final Pattern expression;

        public RegularExpression(Pattern expression) {
            this.expression = expression;
        }

        @Override
        public int numberOfVariables() {
            return 0;
        }

        @Override
        public Option<List<Object>> unapply(String s) {
            if (expression.matcher(s).matches()) {
                return new Option.Some<>(Arrays.asList());
            } else {
                return new Option.None<>();
            }
        }
    }
}
