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

package org.smallibs.suitcase.matching;

import junit.framework.TestCase;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static org.smallibs.suitcase.pattern.Cases._;
import static org.smallibs.suitcase.pattern.xml.Xml.*;

public class XmlMatcherTest {

    @Test
    public void shouldMatchAnElement() throws Exception {
        final Element rootElement = getElementFromSampleDocument();

        final Matcher<Node, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, _)).then.constant(true);
        matcher.caseOf(_).then.constant(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldMatchAnElementNamed_A() throws Exception {
        final Element rootElement = getElementFromSampleDocument();

        final Matcher<Node, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", _)).then.constant(true);
        matcher.caseOf(_).then.constant(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementNamed_B() throws Exception {
        final Element rootElement = getElementFromSampleDocument();

        final Matcher<Node, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("B", _)).then.constant(true);
        matcher.caseOf(_).then.constant(false);

        TestCase.assertFalse(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementNamed_A_with_Text() throws Exception {
        final Element rootElement = getElementFromSampleDocument();

        final Matcher<Node, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", Seq(Text(_)))).then.constant(true);
        matcher.caseOf(_).then.constant(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementNamed_A_with_ExactText() throws Exception {
        final Element rootElement = getElementFromSampleDocument();

        final Matcher<Node, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", Seq(Text("Text in A")))).then.constant(true);
        matcher.caseOf(_).then.constant(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    @Test
    public void shouldNotMatchAnElementNamed_A_with_ExactTextAndB() throws Exception {
        final Element rootElement = getElementFromSampleDocument();

        final Matcher<Node, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A", Seq(Text("Text in A"),Tag("B",Seq(Text("Text in B"),Tag("C",_),Tag("D",_)))))).then.constant(true);
        matcher.caseOf(_).then.constant(false);

        TestCase.assertTrue(matcher.match(rootElement));
    }

    private Element getElementFromSampleDocument() throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        try (InputStream resourceAsStream = XmlMatcherTest.class.getResourceAsStream("/sample.xml")) {
            final Document document = builder.parse(resourceAsStream);
            return document.getDocumentElement();
        }
    }
}
