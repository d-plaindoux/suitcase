package org.smallibs.suitcase.cases;

import org.smallibs.suitcase.utils.Pair;

public interface Result<R> {

    static <R> WithoutCapture<R> success(R term) {
        return new WithoutCapture<>(term);
    }

    static <C> WithCapture<C> successAndReturns(C result) {
        return new WithCapture<>(result);
    }

    R resultValue();

    class WithCapture<C> implements Result<C> {

        private final C capturedObject;

        public WithCapture(C capturedObject) {
            this.capturedObject = capturedObject;
        }

        public <S> WithCapture<Pair<C, S>> with(WithCapture<S> result) {
            return new WithCapture<>(new Pair<>(this.capturedObject, result.capturedObject));
        }

        public WithCapture<C> with(@SuppressWarnings("UnusedParameters") WithoutCapture ignore) {
            return this;
        }

        public C resultValue() {
            return capturedObject;
        }
    }

    class WithoutCapture<T> implements Result<T> {

        private final T term;

        public WithoutCapture(T term) {
            this.term = term;
        }

        public <N> WithoutCapture<N> with(WithoutCapture<N> result) {
            return result;
        }

        public <C> WithCapture<C> with(WithCapture<C> result) {
            return new WithCapture<>(result.resultValue());
        }

        public T resultValue() {
            return term;
        }
    }

}
