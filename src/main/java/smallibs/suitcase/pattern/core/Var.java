package smallibs.suitcase.pattern.core;

import smallibs.suitcase.pattern.Case;
import smallibs.suitcase.pattern.Cases;
import smallibs.suitcase.pattern.MatchResult;
import smallibs.suitcase.utils.Option;

public class Var<T> implements Case<T> {


    private final Case<T> value;

    public Var(Object value) {
        this.value = Cases.fromObject(value);
    }

    @Override
    public Option<MatchResult> unapply(T t) {
        final Option<MatchResult> unapply = this.value.unapply(t);
        if (unapply.isNone()) {
            return new Option.None<>();
        } else {
            return new Option.Some<>(new MatchResult(unapply.value().getReturnedObject(), t));
        }
    }
}
