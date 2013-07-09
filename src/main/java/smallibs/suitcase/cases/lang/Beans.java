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

            final Method[] methods = object.getClass().getDeclaredMethods();
            for (Method field : methods) {
                final Option<MatchResult> unapply = unapplyMethod(object, field);
                if (!unapply.isNone()) {
                    return unapply;
                }
            }

            return new Option.None<>();
        }

        // =============================================================================================================

        private Option<MatchResult> unapplyMethod(Object object, Method method) {
            final String fieldName = getFieldName(method);

            if (fieldName != null) {
                final Option<MatchResult> unapplyName = unapplyName(fieldName);
                if (!unapplyName.isNone()) {
                    final Option<MatchResult> unapplyValue = unapplyMethodValue(object, method);
                    if (!unapplyValue.isNone()) {
                        return new Option.Some<>(new MatchResult(method).with(unapplyName.value()).with(unapplyValue.value()));
                    }
                }
            }

            return new Option.None<>();
        }

        private Option<MatchResult> unapplyField(Object object, Field field) {
            final Option<MatchResult> unapplyName = unapplyName(field.getName());
            if (!unapplyName.isNone()) {
                final Option<MatchResult> unapplyValue = unapplyFieldValue(object, field);
                if (!unapplyValue.isNone()) {
                    return new Option.Some<>(new MatchResult(field).with(unapplyName.value()).with(unapplyValue.value()));
                }
            }

            return new Option.None<>();
        }

        // =============================================================================================================

        private Option<MatchResult> unapplyName(String name) {
            return nameCase.unapply(name);
        }

        private Option<MatchResult> unapplyMethodValue(Object object, Method method) {
            if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
                try {
                    return valueCase.unapply(method.invoke(object));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return new Option.None<>();
                }
            } else {
                return new Option.None<>();
            }
        }

        private Option<MatchResult> unapplyDirectFieldValue(Object object, Field field) {
            try {
                return valueCase.unapply(field.get(object));
            } catch (IllegalAccessException consume) {
                return new Option.None<>();
            }
        }

        private Option<MatchResult> unapplyGetterFieldValue(Object object, Field field) {
            try {
                final Method methodDef = object.getClass().getMethod(getGetterName(field));
                return valueCase.unapply(methodDef.invoke(object));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                return new Option.None<>();
            }
        }

        private Option<MatchResult> unapplyFieldValue(Object object, Field field) {
            final Option<MatchResult> unapply;

            if (Modifier.isPublic(field.getModifiers())) {
                unapply = unapplyDirectFieldValue(object, field);
            } else {
                unapply = unapplyGetterFieldValue(object, field);
            }

            return unapply;
        }

        private String getGetterName(Field field) {
            return "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        }

        private String getFieldName(Method method) {
            final String name = method.getName();
            if (name.startsWith("get") && name.length() > 3) {
                return name.substring(3, 4).toLowerCase() + name.substring(4);
            } else {
                return null;
            }
        }
    }

}
