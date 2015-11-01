package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;
import java.util.Optional;

public class Var<T> implements Case<T> {

    private final Case<T> value;

    public Var(Object value) {
        this.value = Cases.fromObject(value);
    }

    @Override
    public Optional<MatchResult> unapply(T t) {
        final Optional<MatchResult> unapply = this.value.unapply(t);
        if (unapply.isPresent()) {
            return Optional.ofNullable(new MatchResult(unapply.get().matchedObject(), t).with(unapply.get()));
        } else {
            return Optional.empty();
        }
    }

    public Case<T> getValue() {
        return value;
    }

    @Override
    public int variables() {
        return value.variables() + 1;
    }
}
