package org.smallibs.suitcase.cases;

public interface CaseWithResult<T, R> extends Case<T> {

    R getResult();

}
