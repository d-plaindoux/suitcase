package smallibs.suitcase.cases.core;

import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
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
            return Option.None();
        } else {
            return Option.Some(new MatchResult(unapply.value().matchedObject(), t));
        }
    }

    public Case<T> getValue() {
        return value;
    }
}
