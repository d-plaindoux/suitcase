/*
 * Copyright (C)2013 D. Plaindoux.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; see the file COPYING.  If not, write to
 * the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package smallibs.suitcase.matching;

import junit.framework.TestCase;
import org.junit.Test;
import smallibs.suitcase.pattern.xml.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static smallibs.suitcase.pattern.Cases._;
import static smallibs.suitcase.pattern.xml.Xml.*;

public class XmlMatcherTest {

    @Test
    public void shouldMatchAsingleElement() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldMatchAnElement() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, _, _)).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldMatchElementNamedA() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", _, _)).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldMatchElements() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(OptRep(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldMatchElements2() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(_, OptRep(_))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldMatchAnElementNamedAAndMayBeAContent() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", OptRep(_))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementNamedB() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("B", _)).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertFalse(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementNamed_A_with_Text() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", Text(_), _)).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementNamed_A_with_ExactText() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", Text("Text in A"), OptRep(_))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementNamed_A_with_ExactTextAndB() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/sample.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", Text("Text in A"), Tag("B", Text("Text in B"), Tag("C",OptRep(_)), Tag("D", OptRep(_))))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    // =================================================================================================================

    @Test
    public void shouldNotMatchAnEmptyElement() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/empty.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A")).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementMayBeEmpty() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/empty.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A",Opt(_))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementMayBeEmpty2() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/empty.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A",OptRep(_))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    // =================================================================================================================

    @Test
    public void shouldNotMatchAnElementWithTwoTags() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/single.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A",_,_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementWithTwoTags2() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/single.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A",Opt(Text(_)),_,_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementWithTwoTags3() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/single.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A",Opt(Text(_)),OptRep(Tag(_)))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementWithTwoTags4() throws Exception {
        final NodeList rootElement = getElementFromSampleDocument("/single.xml");

        final Matcher<NodeList, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A",Opt(Text(_)),Rep(Tag(_)))).then.value(true);
        matcher.caseOf(_).then.value(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    // =================================================================================================================

    private NodeList getElementFromSampleDocument(String file) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        try (InputStream resourceAsStream = XmlMatcherTest.class.getResourceAsStream(file)) {
            final Document document = builder.parse(resourceAsStream);
            return Xml.toNodeList(document.getDocumentElement());
        }
    }
}
