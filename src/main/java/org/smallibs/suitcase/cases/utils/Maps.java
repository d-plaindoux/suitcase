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

package org.smallibs.suitcase.cases.utils;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;
import org.smallibs.suitcase.cases.core.Cases;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Maps {
    public static <T1, T2> Case<Map<T1, T2>> Map(Object... entries) {
        return new MapCase<>(entries);
    }

    public static <T1, T2> Case<Map<T1, T2>> Entry(T1 o1, Object o2) {
        return new EntryCase<>(o1, o2);
    }

    // ------------------------------------------------------------------------------------------------------------------
    // Private case classes
    // ------------------------------------------------------------------------------------------------------------------

    @CaseType(Map.class)
    private static class MapCase<T1, T2> implements Case<Map<T1, T2>> {

        private final List<Case<Map<T1, T2>>> entries;

        MapCase(Object... contents) {
            this.entries = new ArrayList<>();
            for (Object content : contents) {
                final Case<Map<T1, T2>> aCase = Cases.fromObject(content);
                this.entries.add(aCase);
            }
        }

        @Override
        public Optional<MatchResult> unapply(Map<T1, T2> map) {
            final MatchResult matchResult = new MatchResult(map);
            for (Case<Map<T1, T2>> entry : this.entries) {
                final Optional<MatchResult> unapply = entry.unapply(map);
                if (!unapply.isPresent()) {
                    return unapply;
                } else {
                    matchResult.with(unapply.get());
                }
            }
            return Optional.ofNullable(matchResult);
        }

        @Override
        public int variables() {
            int variables = 0;

            for (Case<?> aCase : this.entries) {
                variables += aCase.variables();
            }

            return variables;
        }
    }

    private static class EntryCase<T1, T2> implements Case<Map<T1, T2>> {

        private final T1 key;
        private final Case<T2> valCase;

        EntryCase(T1 key, Object o2) {
            this.key = key;
            this.valCase = Cases.fromObject(o2);
        }

        @Override
        public Optional<MatchResult> unapply(Map<T1, T2> map) {
            if (map.containsKey(this.key)) {
                return this.valCase.unapply(map.get(this.key));
            } else {
                return Optional.empty();
            }
        }

        @Override
        public int variables() {
            return valCase.variables();
        }
    }
}
