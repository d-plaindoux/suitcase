package org.smallibs.suitcase.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.smallibs.suitcase.match.models.T;

import static org.smallibs.suitcase.cases.core.Cases.Any;
import static org.smallibs.suitcase.cases.core.Cases.Var;
import static org.smallibs.suitcase.cases.lang.Beans.Att;

public class BeanMatcherTest {

    @Test
    public void shouldFindAttributeByName() throws Exception {
        final T t = new T(1, 2);

        Matcher<T, Boolean> attribute = Matcher.create();

        attribute.caseOf(Att("v1", Any())).then(true);
        attribute.caseOf(Any()).then(false);

        TestCase.assertTrue(attribute.match(t));
    }

    @Test
    public void shouldNotFindAttributeByName() throws Exception {
        final T t = new T(1, 2);

        Matcher<T, Boolean> attribute = Matcher.create();

        attribute.caseOf(Att("v4", Any())).then(true);
        attribute.caseOf(Any()).then(false);

        TestCase.assertFalse(attribute.match(t));
    }

    @Test
    public void shouldFindAttributeByMethod() throws Exception {
        final T t = new T(1, 2);

        Matcher<T, Boolean> attribute = Matcher.create();

        attribute.caseOf(Att("v2", Any())).then(true);
        attribute.caseOf(Any()).then(false);

        TestCase.assertTrue(attribute.match(t));
    }

    @Test
    public void shouldBNotFindPrivateAttribute() throws Exception {
        final T t = new T(1, 2);

        Matcher<T, Boolean> attribute = Matcher.create();

        attribute.caseOf(Att("v3", Any())).then(true);
        attribute.caseOf(Any()).then(false);

        TestCase.assertFalse(attribute.match(t));
    }

    @Test
    public void shouldFindAttributeValueByName() throws Exception {
        final T t = new T(1, 2);

        Matcher<T, Integer> attribute = Matcher.create();

        attribute.caseOf(Att("v1", Var(Integer.class))).then(e -> e);
        attribute.caseOf(Any()).then(0);

        TestCase.assertEquals((int) attribute.match(t), 1);
    }

    @Test
    public void shouldFindAttributeValueByMethod() throws Exception {
        final T t = new T(1, 2);

        Matcher<T, Integer> attribute = Matcher.create();

        attribute.caseOf(Att("v2", Var(Integer.class))).then(e -> e);
        attribute.caseOf(Any()).then(0);

        TestCase.assertEquals((int) attribute.match(t), 2);
    }

    //
    // Class Test
    //

}
