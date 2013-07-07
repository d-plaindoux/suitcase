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

        private Option<MatchResult> getField(Object object) {
            final Field[] fields = object.getClass().getDeclaredFields();

            for (Field field : fields) {
                final Option<MatchResult> unapply = nameCase.unapply(field.getName());
                if (!unapply.isNone()) {
                    return new Option.Some<>(new MatchResult(field).with(unapply.value()));
                }
            }

            return new Option.None<>();
        }

        @Override
        public Option<MatchResult> unapply(Object o) {
            final Option<MatchResult> option = getField(o);

            if (option.isNone()) {
                return new Option.None<>();
            }

            final Field field = option.value().matchedObject(Field.class);

            if (Modifier.isPublic(field.getModifiers())) {
                try {
                    final Object value = field.get(o);
                    final Option<MatchResult> unapply = valueCase.unapply(value);
                    if (!unapply.isNone()) {
                        return new Option.Some<>(new MatchResult(o).with(option.value()).with(unapply.value()));
                    }
                } catch (IllegalAccessException consume) {
                    // Ignore
                }
            } else {
                try {
                    final String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    final Method methodDef = o.getClass().getMethod(methodName);
                    final Object value = methodDef.invoke(o);
                    final Option<MatchResult> unapply = valueCase.unapply(value);
                    if (!unapply.isNone()) {
                        return new Option.Some<>(new MatchResult(o).with(option.value()).with(unapply.value()));
                    }
                } catch (NoSuchMethodException e) {
                    // Ignore
                } catch (InvocationTargetException e) {
                    // Ignore
                } catch (IllegalAccessException e) {
                    // Ignore
                }
            }

            return new Option.None<>();
        }

    }

    // =================================================================================================================

}
