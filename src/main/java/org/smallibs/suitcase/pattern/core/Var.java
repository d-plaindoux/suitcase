package org.smallibs.suitcase.pattern.core;

import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.utils.Option;

import java.util.Arrays;
import java.util.List;

public class Var<T> implements Case<T> {


    private final Case<Object> value;

    public Var() {
        this(Cases.any());
    }

    public Var(Object value) {
        this.value = Cases.reify(value);
    }

    @Override
    public Option<List<Object>> unapply(T t) {
        if (this.value.unapply(t).isNone()) {
            return new Option.None<>();
        } else {
            return new Option.Some<>(Arrays.<Object>asList(t));
        }
    }
}
