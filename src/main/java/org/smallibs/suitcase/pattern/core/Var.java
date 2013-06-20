package org.smallibs.suitcase.pattern.core;

import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.utils.Option;

import java.util.Arrays;
import java.util.List;

public class Var<T> implements Case<T> {


    private final Case<T> value;

    public Var(Object value) {
        this.value = Cases.fromObject(value);
    }

    @Override
    public int numberOfVariables() {
        return 1 + this.value.numberOfVariables();
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
