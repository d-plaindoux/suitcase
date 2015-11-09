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
import org.smallibs.suitcase.cases.core.Cases;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

public interface Beans {

    static <T, R, C> Case.WithoutCapture<T, C> Att(String nameCase, Case.WithoutCapture<R, C> valueCase) {
        return Att(Cases.Constant(nameCase), valueCase);
    }

    static <T, R, C> Case.WithoutCapture<T, C> Att(Case<String, ?> nameCase, Case.WithoutCapture<R, C> valueCase) {
        return Case.WithoutCapture.adapt(new BeanAtt<>(nameCase, valueCase));
    }

    static <T, R, C> Case.WithCapture<T, C> Att(String nameCase, Case.WithCapture<R, C> valueCase) {
        return Att(Cases.Constant(nameCase), valueCase);
    }

    static <T, R, C> Case.WithCapture<T, C> Att(Case<String, ?> nameCase, Case.WithCapture<R, C> valueCase) {
        return Case.WithCapture.adapt(new BeanAtt<>(nameCase, valueCase));
    }

    // =================================================================================================================
    // Bean attribute case class
    // =================================================================================================================

    class BeanAtt<T, R, C> implements Case<T, C> {

        private final Case<String, ?> nameCase;
        private final Case<R, C> valueCase;

        public BeanAtt(Case<String, ?> nameCase, Case<R, C> valueCase) {
            this.nameCase = nameCase;
            this.valueCase = valueCase;
        }

        @Override
        public Optional<C> unapply(T object) {
            final Field[] fields = object.getClass().getDeclaredFields();

            for (Field field : fields) {
                final Optional<C> optional = unapplyField(object, field);
                if (optional.isPresent()) {
                    return optional;
                }
            }

            final Method[] methods = object.getClass().getDeclaredMethods();
            for (Method field : methods) {
                final Optional<C> optional = unapplyMethod(object, field);
                if (optional.isPresent()) {
                    return optional;
                }
            }

            return Optional.empty();
        }

        // =============================================================================================================

        private Optional<C> unapplyMethod(T object, Method method) {
            final String fieldName = getFieldName(method);

            if (fieldName != null) {
                final Optional<?> unapplyName = unapplyName(fieldName);
                if (unapplyName.isPresent()) {
                    return unapplyMethodValue(object, method);
                }
            }

            return Optional.empty();
        }

        private Optional<C> unapplyField(T object, Field field) {
            final Optional<?> unapplyName = unapplyName(field.getName());
            if (unapplyName.isPresent()) {
                return unapplyFieldValue(object, field);
            }

            return Optional.empty();
        }

        // =============================================================================================================

        private Optional<?> unapplyName(String name) {
            return nameCase.unapply(name);
        }

        private Optional<C> unapplyMethodValue(T object, Method method) {
            if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
                try {
                    //noinspection unchecked
                    return valueCase.unapply((R) method.invoke(object));
                } catch (IllegalAccessException | InvocationTargetException | ClassCastException consume) {
                    // Ignore
                }
            }

            return Optional.empty();
        }

        private Optional<C> unapplyFieldValue(T object, Field field) {
            if (Modifier.isPublic(field.getModifiers())) {
                return unapplyDirectFieldValue(object, field);
            } else {
                return unapplyGetterFieldValue(object, field);
            }
        }

        private Optional<C> unapplyDirectFieldValue(T object, Field field) {
            try {
                //noinspection unchecked
                return valueCase.unapply((R) field.get(object));
            } catch (IllegalAccessException | ClassCastException consume) {
                // Ignore
            }

            return Optional.empty();
        }

        private Optional<C> unapplyGetterFieldValue(T object, Field field) {
            try {
                final Method methodDef = object.getClass().getMethod(getGetterName(field));
                //noinspection unchecked
                return valueCase.unapply((R) methodDef.invoke(object));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassCastException consume) {
                // Ignore
            }
            return Optional.empty();
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
