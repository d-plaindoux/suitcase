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

package org.smallibs.suitcase.cases.lang;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;
import org.smallibs.suitcase.cases.core.Cases;
import java.util.Optional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class Beans {

    public static Case<Object> Obj(Object... contents) {
        return new Bean(contents);
    }

    public static Case<Object> Att(Object nameCase, Object valueCase) {
        return new BeanAtt(nameCase, valueCase);
    }

    // =================================================================================================================
    // Obj case class
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
        public Optional<MatchResult> unapply(Object o) {
            final MatchResult matchResult = new MatchResult(null);

            for (Case<Object> content : contentCases) {
                final Optional<MatchResult> unapply = content.unapply(o);
                if (!unapply.isPresent()) {
                    return unapply;
                } else {
                    matchResult.with(unapply.get());
                }
            }

            return Optional.ofNullable(new MatchResult(o).with(matchResult));
        }


        @Override
        public List<Class> variableTypes() {
            final List<Class> classes = new ArrayList<>();

            for (Case<?> aCase : this.contentCases) {
                classes.addAll(aCase.variableTypes());
            }

            return classes;
        }
    }

    // =================================================================================================================
    // Obj attribute case class
    private static class BeanAtt implements Case<Object> {

        private final Case<Object> nameCase;
        private final Case<Object> valueCase;

        public BeanAtt(Object name, Object value) {
            this.nameCase = Cases.fromObject(name);
            this.valueCase = Cases.fromObject(value);
        }

        @Override
        public Optional<MatchResult> unapply(Object object) {
            final Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                final Optional<MatchResult> unapply = unapplyField(object, field);
                if (unapply.isPresent()) {
                    return unapply;
                }
            }

            final Method[] methods = object.getClass().getDeclaredMethods();
            for (Method field : methods) {
                final Optional<MatchResult> unapply = unapplyMethod(object, field);
                if (unapply.isPresent()) {
                    return unapply;
                }
            }

            return Optional.empty();
        }

        // =============================================================================================================

        private Optional<MatchResult> unapplyMethod(Object object, Method method) {
            final String fieldName = getFieldName(method);

            if (fieldName != null) {
                final Optional<MatchResult> unapplyName = unapplyName(fieldName);
                if (unapplyName.isPresent()) {
                    final Optional<MatchResult> unapplyValue = unapplyMethodValue(object, method);
                    if (unapplyValue.isPresent()) {
                        return Optional.ofNullable(new MatchResult(method).with(unapplyName.get()).with(unapplyValue.get()));
                    }
                }
            }

            return Optional.empty();
        }

        private Optional<MatchResult> unapplyField(Object object, Field field) {
            final Optional<MatchResult> unapplyName = unapplyName(field.getName());
            if (unapplyName.isPresent()) {
                final Optional<MatchResult> unapplyValue = unapplyFieldValue(object, field);
                if (unapplyValue.isPresent()) {
                    return Optional.ofNullable(new MatchResult(field).with(unapplyName.get()).with(unapplyValue.get()));
                }
            }

            return Optional.empty();
        }

        // =============================================================================================================

        private Optional<MatchResult> unapplyName(String name) {
            return nameCase.unapply(name);
        }

        private Optional<MatchResult> unapplyMethodValue(Object object, Method method) {
            if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
                try {
                    return valueCase.unapply(method.invoke(object));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }

        private Optional<MatchResult> unapplyDirectFieldValue(Object object, Field field) {
            try {
                return valueCase.unapply(field.get(object));
            } catch (IllegalAccessException consume) {
                return Optional.empty();
            }
        }

        private Optional<MatchResult> unapplyGetterFieldValue(Object object, Field field) {
            try {
                final Method methodDef = object.getClass().getMethod(getGetterName(field));
                return valueCase.unapply(methodDef.invoke(object));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                return Optional.empty();
            }
        }

        private Optional<MatchResult> unapplyFieldValue(Object object, Field field) {
            final Optional<MatchResult> unapply;

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

        @Override
        public List<Class> variableTypes() {
            final List<Class> classes = nameCase.variableTypes();
            classes.addAll(valueCase.variableTypes());
            return classes;
        }
    }

}
