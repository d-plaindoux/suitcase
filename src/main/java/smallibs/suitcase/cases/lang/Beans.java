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

package smallibs.suitcase.cases.lang;

import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.cases.core.Cases;
import smallibs.suitcase.utils.Option;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class Beans {

    public static Case<Object> Bean(Object... contents) {
        return new Bean(contents);
    }

    public static Case<Object> Att(Object nameCase, Object valueCase) {
        return new BeanAtt(nameCase, valueCase);
    }

    // =================================================================================================================
    // Bean case class
    // =================================================================================================================

    private static class Bean implements Case<Object> {

        private final List<Case<Object>> contentCases;

        public Bean(Object[] contents) {
            this.contentCases = new ArrayList<>();
            for (Object content : contents) {
                this.contentCases.add(Cases.fromObject(content));
            }
        }

        @Override
        public Option<MatchResult> unapply(Object o) {
            final MatchResult matchResult = new MatchResult(null);

            for (Case<Object> content : contentCases) {
                final Option<MatchResult> unapply = content.unapply(o);
                if (unapply.isNone()) {
                    return unapply;
                } else {
                    matchResult.with(unapply.value());
                }
            }

            return new Option.Some<>(new MatchResult(o).with(matchResult));
        }
    }

    // =================================================================================================================
    // Bean attribute case class
    // =================================================================================================================

    private static class BeanAtt implements Case<Object> {

        private final Case<Object> nameCase;
        private final Case<Object> valueCase;

        public BeanAtt(Object name, Object value) {
            this.nameCase = Cases.fromObject(name);
            this.valueCase = Cases.fromObject(value);
        }

        @Override
        public Option<MatchResult> unapply(Object object) {
            final Field[] fields = object.getClass().getDeclaredFields();

            for (Field field : fields) {
                final Option<MatchResult> unapply = unapplyField(object, field);
                if (!unapply.isNone()) {
                    return unapply;
                }
            }

            return new Option.None<>();
        }

        private Option<MatchResult> unapplyField(Object o, Field field) {
            final Option<MatchResult> unapplyName = unapplyFieldName(field);
            if (!unapplyName.isNone()) {
                final Option<MatchResult> unapplyValue = unapplyFieldValue(o, field);
                if (!unapplyValue.isNone()) {
                    return new Option.Some<>(new MatchResult(field).with(unapplyName.value()).with(unapplyValue.value()));
                }
            }

            return new Option.None<>();
        }

        private Option<MatchResult> unapplyFieldName(Field field) {
            return nameCase.unapply(field.getName());
        }

        private Option<MatchResult> unapplyFieldValue(Object o, Field field) {
            final Option<MatchResult> unapply;

            if (Modifier.isPublic(field.getModifiers())) {
                unapply = unapplyDirectFieldValue(o, field);
            } else {
                unapply = unapplyGetterFieldValue(o, field);
            }

            return unapply;
        }

        private Option<MatchResult> unapplyDirectFieldValue(Object o, Field field) {
            try {
                final Object value = field.get(o);
                return valueCase.unapply(value);
            } catch (IllegalAccessException consume) {
                return new Option.None<>();
            }
        }

        private Option<MatchResult> unapplyGetterFieldValue(Object o, Field field) {
            try {
                final String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                final Method methodDef = o.getClass().getMethod(methodName);
                final Object value = methodDef.invoke(o);
                return valueCase.unapply(value);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                return new Option.None<>();
            }
        }
    }

}
