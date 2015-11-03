package org.smallibs.suitcase.cases;

import org.smallibs.suitcase.utils.Pair;

public interface Result {

    static WithoutCapture success() {
        return new WithoutCapture();
    }

    static <R> WithCapture<R> success(R result) {
        return new WithCapture<>(result);
    }

    interface Combinator<C1, C2, C3> {
        C3 apply(C1 c1, C2 c2);
    }

    class Without implements Combinator<WithoutCapture, WithoutCapture, WithoutCapture> {
        @Override
        public WithoutCapture apply(WithoutCapture withoutCapture, WithoutCapture withoutCapture2) {
            return success();
        }
    }

    class WithLeft<C> implements Combinator<WithCapture<C>, WithoutCapture, WithCapture<C>> {
        @Override
        public WithCapture<C> apply(WithCapture<C> withCapture, WithoutCapture withoutCapture) {
            return withCapture;
        }
    }

    class WithRight<C> implements Combinator<WithoutCapture, WithCapture<C>, WithCapture<C>> {
        @Override
        public WithCapture<C> apply(WithoutCapture withoutCapture, WithCapture<C> withCapture) {
            return withCapture;
        }
    }

    class WithBoth<C1, C2> implements Combinator<WithCapture<C1>, WithCapture<C2>, WithCapture<Pair<C1, C2>>> {
        @Override
        public WithCapture<Pair<C1, C2>> apply(WithCapture<C1> c1WithCapture, WithCapture<C2> c2WithCapture) {
            return success(new Pair<>(c1WithCapture.capturedObject, c2WithCapture.capturedObject));
        }
    }

    class WithCapture<R> {

        private final R capturedObject;

        public WithCapture(R capturedObject) {
            this.capturedObject = capturedObject;
        }

        public <S> WithCapture<Pair<R, S>> with(WithCapture<S> result) {
            return new WithCapture<>(new Pair<>(this.capturedObject, result.capturedObject));
        }

        public WithCapture<R> with(@SuppressWarnings("UnusedParameters") WithoutCapture ignore) {
            return this;
        }

        public R capturedObject() {
            return capturedObject;
        }
    }

    class WithoutCapture {

        public WithoutCapture() {
        }

        public WithoutCapture with(@SuppressWarnings("UnusedParameters") WithoutCapture result) {
            return this;
        }

        public <S> WithCapture<S> with(WithCapture<S> result) {
            return new WithCapture<>(result.capturedObject());
        }
    }

}
