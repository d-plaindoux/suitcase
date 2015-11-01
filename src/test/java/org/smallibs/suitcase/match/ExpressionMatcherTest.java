package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;
import org.smallibs.suitcase.cases.core.ReentrantMatcher;

import java.util.List;
import java.util.Optional;

import static org.smallibs.suitcase.cases.core.Cases.constant;
import static org.smallibs.suitcase.cases.core.Cases.fromObject;
import static org.smallibs.suitcase.cases.core.Cases.var;
import static org.smallibs.suitcase.cases.core.ReentrantMatcher.reentrant;

public class ExpressionMatcherTest {

    @Test
    public void shouldEvalNatExpression() throws Exception {
        Matcher<Expr, Integer> eval = Matcher.create();

        eval.caseOf(ExprCases.Nat(var)).then((Integer i) -> i);
        eval.caseOf(ExprCases.Add(var, var)).then((Expr l, Expr r) -> eval.match(l) + eval.match(r));

        final int match = eval.match(Expr.Nat(1));

        TestCase.assertEquals(match, 1);
    }

    @Test
    public void shouldEvalAddExpression() throws Exception {
        Matcher<Expr, Integer> eval = Matcher.create();

        eval.caseOf(ExprCases.Nat(var)).then((Integer i) -> i);
        eval.caseOf(ExprCases.Add(var, var)).then((Expr l, Expr r) -> eval.match(l) + eval.match(r));

        final int match = eval.match(Expr.Add(Expr.Nat(1), Expr.Nat(2)));

        TestCase.assertEquals(match, 3);
    }

    @Test
    public void shouldEvalAReentrantEvaluatorExpression() throws Exception {
        ReentrantMatcher<Expr, Integer> eval = reentrant(Matcher.create());

        eval.caseOf(ExprCases.Nat(var)).then((Integer i) -> i);
        eval.caseOf(ExprCases.Add(var.of(eval), var.of(eval))).then((Integer l, Integer r) -> l + r);

        final int match = eval.match(Expr.Add(Expr.Nat(1), Expr.Nat(2)));

        TestCase.assertEquals(match, 3);
    }

    public interface Expr {

        static Nat Nat(int val) {
            return new Nat(val);
        }

        static Add Add(Expr left, Expr right) {
            return new Add(left, right);
        }


        class Nat implements Expr {
            final int val;

            public Nat(int val) {
                this.val = val;
            }
        }

        class Add implements Expr {
            final Expr left, right;

            public Add(Expr left, Expr right) {
                this.left = left;
                this.right = right;
            }
        }

    }

    public interface ExprCases {

        static Nat Nat(int val) {
            return new Nat(constant(val));
        }

        static Nat Nat(Case<Integer> val) {
            return new Nat(val);
        }

        static Add Add(Case<Expr> left, Case<Expr> right) {
            return new Add(left, right);
        }

        class Nat implements Case<Expr> {
            private Case<Integer> integerCase;

            public Nat(Case<Integer> object) {
                this.integerCase = fromObject(object);
            }

            @Override
            public Optional<MatchResult> unapply(Expr expr) {
                if (expr instanceof Expr.Nat) {
                    return integerCase.unapply(Expr.Nat.class.cast(expr).val);
                }

                return Optional.empty();
            }

            @Override
            public int variables() {
                return integerCase.variables();
            }
        }

        class Add implements Case<Expr> {
            final Case<Expr> left, right;

            public Add(Case<Expr> l, Case<Expr> r) {
                this.left = l;
                this.right = r;
            }

            @Override
            public Optional<MatchResult> unapply(Expr expr) {
                if (expr instanceof Expr.Add) {
                    final Expr.Add add = Expr.Add.class.cast(expr);
                    return left.unapply(add.left).flatMap(leftResult ->
                                    right.unapply(add.right).map(rightResult ->
                                                    new MatchResult(expr).with(leftResult).with(rightResult)
                                    )
                    );
                }

                return Optional.empty();
            }

            @Override
            public int variables() {
                return left.variables() + right.variables();
            }
        }
    }
}
