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

import junit.framework.TestCase;
import org.junit.Test;
import smallibs.suitcase.cases.xml.Xml;

public class XmlGenLexTest {

    @Test
    public void shouldMatchSimpleTag() {
        final Boolean match = Xml.validate(Xml.stream("<a/>"));
        TestCase.assertTrue(match);
    }

    @Test
    public void shouldMatchSimpleTagWithAttribute() {
        final Boolean match = Xml.validate(Xml.stream("<a b='c'/>"));
        TestCase.assertTrue(match);
    }

    @Test
    public void shouldMatchSimpleTagWithAttributes() {
        final Boolean match = Xml.validate(Xml.stream("<a b='c' d='e'/>"));
        TestCase.assertTrue(match);
    }

    @Test
    public void shouldMatchEmptyTag() {
        final Boolean match = Xml.validate(Xml.stream("<a></a>"));
        TestCase.assertTrue(match);
    }

    @Test
    public void shouldMatchEmptyTagWithAttribute() {
        final Boolean match = Xml.validate(Xml.stream("<a b=\"c\"></a>"));
        TestCase.assertTrue(match);
    }

    @Test
    public void shouldMatchTagWithText() {
        final Boolean match = Xml.validate(Xml.stream("<a>Hello World!</a>"));
        TestCase.assertTrue(match);
    }

    @Test
    public void shouldMatchTagWithTextAndElement() {
        final Boolean match = Xml.validate(Xml.stream("<a>Hello <toto/>!</a>"));
        TestCase.assertTrue(match);
    }

    @Test(expected = MatchingException.class)
    public void shouldNotdMatchSimpleTag2() {
        Xml.validate(Xml.stream("<a></b>"));
    }

    @Test
    public void shouldMatchTagWithComment() {
        final Boolean match = Xml.validate(Xml.stream("<a><!-- Hello World! --></a>"));
        TestCase.assertTrue(match);
    }

    @Test
    public void shouldMatchTagWithCData() {
        final Boolean match = Xml.validate(Xml.stream("<a><![CDATA[ Hello <world/>! ]]></a>"));
        TestCase.assertTrue(match);
    }
}
