package org.smallibs.suitcase.cases.core;

import org.smallibs.suitcase.cases.Result;
import org.smallibs.suitcase.cases.Result.WithCapture;
import org.smallibs.suitcase.cases.Result.WithoutCapture;
import org.smallibs.suitcase.utils.Apply.Apply2;
import org.smallibs.suitcase.utils.Pair;

import static org.smallibs.suitcase.cases.Result.success;

public interface Combinators {

    interface Combination2<C1, C2, R> {
        R apply(C1 c1, C2 c2);

        class None<C1, C2> implements Combination2<WithoutCapture<C1>, WithoutCapture<C2>, WithoutCapture<Pair<C1, C2>>> {
            @Override
            public WithoutCapture<Pair<C1,C2>> apply(WithoutCapture<C1> withoutCapture1, WithoutCapture<C2> withoutCapture2) {
                return success(new Pair<>(withoutCapture1.resultValue(), withoutCapture2.resultValue()));
            }
        }

        class With1<C1, C2> implements Combination2<WithCapture<C1>, WithoutCapture<C2>, WithCapture<C1>> {
            @Override
            public WithCapture<C1> apply(WithCapture<C1> withCapture, WithoutCapture<C2> withoutCapture) {
                return withCapture;
            }
        }

        class With2<C1, C2> implements Combination2<WithoutCapture<C1>, WithCapture<C2>, WithCapture<C2>> {
            @Override
            public WithCapture<C2> apply(WithoutCapture<C1> withoutCapture, WithCapture<C2> withCapture) {
                return withCapture;
            }
        }

        class All<C1, C2> implements Combination2<WithCapture<C1>, WithCapture<C2>, WithCapture<Apply2<C1, C2>>> {
            @Override
            public WithCapture<Apply2<C1, C2>> apply(WithCapture<C1> c1WithCapture, WithCapture<C2> c2WithCapture) {
                return Result.successAndReturns(new Apply2<>(c1WithCapture.resultValue(), c2WithCapture.resultValue()));
            }
        }
    }
}
