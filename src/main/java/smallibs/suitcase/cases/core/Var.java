package smallibs.suitcase.cases.core;

import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.utils.Option;

import java.util.List;

public class Var<T> implements Case<T> {

    private final Class<?> aClass;
    private final Case<T> value;

    public Var(Class<?> aClass, Object value) {
        this.aClass = aClass;
        this.value = Cases.fromObject(value);
    }

    public Var(Object value) {
        this(value.getClass(), value);
    }

    @Override
    public Option<MatchResult> unapply(T t) {
        final Option<MatchResult> unapply = this.value.unapply(t);
        if (unapply.isPresent()) {
            return Option.Some(new MatchResult(unapply.value().matchedObject(), t).with(unapply.value()));
        } else {
            return Option.None();
        }
    }

    public Case<T> getValue() {
        return value;
    }

    @Override
    public List<Class> variableTypes() {
        final List<Class> classes = value.variableTypes();
        classes.add(0, this.aClass);
        return classes;
    }
}
