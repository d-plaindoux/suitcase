package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Case;

import java.util.Optional;
import java.util.function.Function;

public interface TypeCase {

    static <P, T extends P> Case0<P, Boolean> of(Class<T> type) {
        return new Case0<>(
                e -> e.getClass().isAssignableFrom(type) ? Optional.of(true) : Optional.empty()
        );
    }

    static <P, T extends P, E> Case1<P, P, E> of(Class<T> type, Function<T, E> get) {
        return new Case1<>(
                e -> e.getClass().isAssignableFrom(type) ? Optional.of(e) : Optional.empty(),
                e -> get.apply(type.cast(e))
        );
    }

    static <P, T extends P, E1, E2> Case2<P, P, E1, E2> of(Class<T> type, Function<T, E1> get1, Function<T, E2> get2) {
        return new Case2<>(
                e -> e.getClass().isAssignableFrom(type) ? Optional.of(e) : Optional.empty(),
                e -> get1.apply(type.cast(e)),
                e -> get2.apply(type.cast(e))
        );
    }

}
