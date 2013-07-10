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

package smallibs.suitcase.match;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import smallibs.suitcase.cases.xml.Dom;
import smallibs.suitcase.utils.Function;
import smallibs.suitcase.utils.Function2;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static smallibs.suitcase.cases.core.Cases._;
import static smallibs.suitcase.cases.core.Cases.var;
import static smallibs.suitcase.cases.xml.Dom.*;

public class XmlMatcherTest {

    // =================================================================================================================
    // SEQ
    // =================================================================================================================

    @Test
    public void shouldMatchEmptyTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Empty).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchEmptyTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(_)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchSeqEmptyTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Empty)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchSeqEmptiesTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Empty, Empty)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchSingleTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchSingleTerm2() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(_, Empty)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }


    @Test
    public void shouldMatchSingleTerm3() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Empty, _)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchMultipleTerms() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(_, _)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchMultipleTerms1() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(_)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchMultipleTerms2() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(_, _, _)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchSequenceWithATagTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Tag(_))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchSequenceWithATagTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Text(_))).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchSequenceWithATextTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithTextOnly.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Tag(_))).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchSequenceWithATextTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithTextOnly.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Text(_))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchSequenceWithATagTermAndText() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(Tag(_), Text(_))).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    // =================================================================================================================
    // TAG
    // =================================================================================================================

    @Test
    public void shouldMatchEmptyTagTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchEmptyNamedTagTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("A")).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchEmptyNamedTagTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag("B")).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchTextTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Text(_)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchEmptyTagTerm1() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchEmptyTagTerm2() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, _)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchEmptyTagTerm3() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchTagWithAnAttribute() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithAttributes.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, Att(_, _))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchTagWithNoAttribute() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, Att(_, _))).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchTagWithNamedAttribute() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithAttributes.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, Att("a", _))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchTagWithNamedAttribute() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithAttributes.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, Att("c", _))).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchTagWithValuedAttribute() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithAttributes.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, Att(_, "vA"))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchTagWithValuedAttribute() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithAttributes.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, Att(_, "vC"))).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }


    @Test
    public void shouldMatchTagWithAttributes() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithAttributes.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_, Att("b", _), Att("a", _))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    // =================================================================================================================
    // TEXT
    // =================================================================================================================

    @Test
    public void shouldMatchSimpleTextTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithTextOnly.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Text(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchSimpleSpecificTextTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithTextOnly.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Text("Hello, World!")).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchSimpleSpecificTextTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithTextOnly.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Text("Hello, not that World!")).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchTagTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithTextOnly.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Tag(_)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    // =================================================================================================================
    // OPT
    // =================================================================================================================

    @Test
    public void shouldMatchEmptyTermUsingOpt() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Opt(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchTagTermUsingOpt1() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Opt(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchTagTermUsingOpt2() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Opt(Tag("A"))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldNotMatchTagTermUsingOpt() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Opt(Tag("B"))).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    // =================================================================================================================
    // OPTREP
    // =================================================================================================================

    @Test
    public void shouldMatchEmptyTermUsingOptRep() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(OptRep(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchTagsUsingOptRep() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(OptRep(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchOnlyTagsUsingOptRep() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(OptRep(Text(_)), OptRep(Tag(_)))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    // =================================================================================================================
    // REP
    // =================================================================================================================

    @Test
    public void shouldNotMatchEmptyTermUsingRep() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Rep(_)).then.value(false);
        matcher.caseOf(_).then.value(true);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchTagsUsingRep() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Rep(_)).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldMatchOnlyTagsUsingRep() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(OptRep(Text(_)), Rep(Tag(_)))).then.value(true);
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    // =================================================================================================================
    // Xml term capture
    // =================================================================================================================

    @Test
    public void shouldCaptureEmptyTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(Empty)).then.function(new Function<XmlTerm, Boolean>() {
            public Boolean apply(XmlTerm o) {
                return o.size() == 0;
            }
        });
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldCaptureTagTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/empty.xml").getDocumentElement());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(Tag(_))).then.function(new Function<XmlTerm, Boolean>() {
            public Boolean apply(XmlTerm o) {
                return o.size() == 1 && o.next().getNodeType() == Node.ELEMENT_NODE;
            }
        });
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldCaptureTextTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/tagWithTextOnly.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(Text(_))).then.function(new Function<XmlTerm, Boolean>() {
            public Boolean apply(XmlTerm o) {
                return o.size() == 1 && o.next().getNodeType() == Node.TEXT_NODE;
            }
        });
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldCapturedTagsTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(Seq(Tag(_), Tag(_)))).then.function(new Function<XmlTerm, Boolean>() {
            public Boolean apply(XmlTerm o) {
                return o.size() == 2 && o.next().getNodeType() == Node.ELEMENT_NODE && o.next().getNodeType() == Node.ELEMENT_NODE;
            }
        });
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldCapturedTagsTermUsingOptRep() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(var.of(OptRep(Tag(_)))).then.function(new Function<XmlTerm, Boolean>() {
            public Boolean apply(XmlTerm o) {
                return o.size() == 2 && o.next().getNodeType() == Node.ELEMENT_NODE && o.next().getNodeType() == Node.ELEMENT_NODE;
            }
        });
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    @Test
    public void shouldCapturedEachTagsTerm() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simple.xml").getDocumentElement().getChildNodes());
        final Matcher<XmlTerm, Boolean> matcher = Matcher.create();

        matcher.caseOf(Seq(var, var)).then.function(new Function2<XmlTerm, XmlTerm, Boolean>() {
            public Boolean apply(XmlTerm o1, XmlTerm o2) {
                return o1.size() == 1 && o1.next().getNodeType() == Node.ELEMENT_NODE &&
                        o2.size() == 1 && o2.next().getNodeType() == Node.ELEMENT_NODE;
            }
        });
        matcher.caseOf(_).then.value(false);

        assertTrue(matcher.match(term));
    }

    // =================================================================================================================
    // Full test
    // =================================================================================================================

    private Matcher<XmlTerm, Integer> getNumberOfChars() {
        final Matcher<XmlTerm, Integer> numberOfChars = Matcher.<XmlTerm, Integer>create();

        numberOfChars.caseOf(Text(var)).then.function(
                new Function<String, Integer>() {
                    public Integer apply(String o) {
                        return o.length();
                    }
                }).
                caseOf(Tag(_, var.of(OptRep(_)))).then.function(
                new Function<XmlTerm, Integer>() {
                    public Integer apply(XmlTerm o) {
                        return numberOfChars.match(o);
                    }
                }).
                caseOf(Seq(var, var.of(Rep(_)))).then.function(
                new Function2<XmlTerm, XmlTerm, Integer>() {
                    public Integer apply(XmlTerm o1, XmlTerm o2) {
                        return numberOfChars.match(o1) + numberOfChars.match(o2);
                    }
                }).
                caseOf(Empty).then.value(0);

        return numberOfChars;
    }

    @Test
    public void shouldComputeNumberOfChars() throws Exception {
        final XmlTerm term = Dom.toXmlTerm(getDocument("/simpleWithText.xml").getDocumentElement());
        assertEquals(27, getNumberOfChars().match(term).intValue());
    }

    // =================================================================================================================

    private Document getDocument(String file) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        try (InputStream resourceAsStream = XmlMatcherTest.class.getResourceAsStream(file)) {
            return builder.parse(resourceAsStream);
        }
    }
}
