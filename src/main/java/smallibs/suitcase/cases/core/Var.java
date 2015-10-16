package smallibs.suitcase.cases.core;

import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
import java.util.Optional;

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
    public List<Class> variableTypes() {
        final List<Class> classes = value.variableTypes();
        classes.add(0, this.aClass);
        return classes;
    }
}
