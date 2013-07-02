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

package smallibs.suitcase.pattern.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import smallibs.suitcase.pattern.Case;
import smallibs.suitcase.pattern.Cases;
import smallibs.suitcase.pattern.MatchResult;
import smallibs.suitcase.pattern.core.Var;
import smallibs.suitcase.utils.Option;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dom {

    public static Case<XmlTerm> Tag(Object name, Object... content) {
        return new Tag(name, Seq(content));
    }

    public static Case<XmlTerm> Text(Object content) {
        return new Text(content);
    }

    public static Case<XmlTerm> OptRep(Object... content) {
        return new OptRep(Seq(content));
    }

    public static Case<XmlTerm> Opt(Object... content) {
        return new Opt(Seq(content));
    }

    public static Case<XmlTerm> Rep(Object... content) {
        return new OptRep(Seq(Seq(content), OptRep(content)));
    }

    public static Case<XmlTerm> Seq(Object... content) {
        return new Seq(content);
    }

    public static Case<XmlTerm> Empty = new Seq();

    public static XmlTerm getTerm(Node node) {
        return new InitialTerm(node);
    }

    public static XmlTerm getTerm(NodeList list) {
        return new InitialTerm(list);
    }

    // =================================================================================================================

    private interface XmlCase extends Case<XmlTerm> {
        // Only for specification
    }

    // =================================================================================================================
    // Atomic pattern dedicated to XML terms
    // =================================================================================================================

    private static abstract class NodeCase implements XmlCase {

        private static boolean checkNodeAvailability(XmlTerm nodeList) {
            if (nodeList.isInitial()) {
                return nodeList.size() == 1;
            } else {
                return nodeList.hasNext();
            }
        }

        @Override
        public Option<MatchResult> unapply(XmlTerm nodeList) {
            final Option<MatchResult> result;

            if (checkNodeAvailability(nodeList)) {
                final Node node = nodeList.secondary().next();

                result = unapplyNode(node);

                if (!result.isNone()) {
                    nodeList.next();
                }
            } else {
                result = new Option.None<>();
            }

            return result;
        }

        abstract Option<MatchResult> unapplyNode(Node node);
    }

    private static class Tag extends NodeCase {
        private final Case<String> name;

        private final Case<XmlTerm> content;

        public Tag(Object name, Case<XmlTerm> content) {
            this.name = Cases.fromObject(name);
            this.content = content;
        }

        @Override
        Option<MatchResult> unapplyNode(Node node) {
            final Option<MatchResult> result;

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) node;
                final Option<MatchResult> unapplyName = name.unapply(element.getTagName());
                if (!unapplyName.isNone()) {
                    final Option<MatchResult> unapplyContent = content.unapply(getTerm(node.getChildNodes()));
                    if (unapplyContent.isNone()) {
                        result = new Option.None<>();
                    } else {
                        result = new Option.Some<>(new MatchResult(new InitialTerm(node)).with(unapplyName.value()).with(unapplyContent.value()));
                    }
                } else {
                    result = new Option.None<>();
                }
            } else {
                result = new Option.None<>();
            }

            return result;
        }
    }

    // =================================================================================================================

    private static class Text extends NodeCase {

        private final Case<String> text;

        public Text(Object text) {
            this.text = Cases.fromObject(text);
        }

        @Override
        Option<MatchResult> unapplyNode(Node node) {
            final Option<MatchResult> result;

            if (node.getNodeType() == Node.TEXT_NODE) {
                final Option<MatchResult> unapplyText = text.unapply(node.getTextContent().trim());
                if (unapplyText.isNone()) {
                    result = new Option.None<>();
                } else {
                    result = new Option.Some<>(new MatchResult(new InitialTerm(node)).with(unapplyText.value()));
                }
            } else {
                result = new Option.None<>();
            }

            return result;
        }
    }

    // =================================================================================================================
    // Sequence pattern
    // =================================================================================================================

    private static class Seq implements XmlCase {

        private List<Case<XmlTerm>> content;

        public Seq(Object... content) {
            this.content = new ArrayList<>();
            for (Object object : content) {
                final Case<XmlTerm> aCase = Cases.fromObject(object);
                this.content.add(aCase);
            }
        }

        private boolean isXmlCase(Case<XmlTerm> aCase) {
            if (aCase instanceof Var) {
                return isXmlCase(((Var<XmlTerm>) aCase).getValue());
            } else {
                return aCase instanceof XmlCase;
            }
        }

        @Override
        public Option<MatchResult> unapply(XmlTerm nodeList) {
            final XmlTerm secondary = nodeList.secondary();
            final MatchResult result = new MatchResult(null);

            for (Case<XmlTerm> aContent : content) {
                final Option<MatchResult> unapply;

                if (isXmlCase(aContent)) {
                    unapply = aContent.unapply(secondary);
                } else if (secondary.hasNext()) {
                    unapply = aContent.unapply(new InitialTerm(secondary.next()));
                } else {
                    unapply = new Option.None<>();
                }

                if (unapply.isNone()) {
                    return unapply;
                } else {
                    result.with(unapply.value());
                }
            }

            if (nodeList.isInitial() && secondary.hasNext()) {
                return new Option.None<>();
            } else {
                final List<Node> matched = new ArrayList<>();
                while (nodeList.size() > secondary.size()) {
                    matched.add(nodeList.next());
                }
                return new Option.Some<>(new MatchResult(new InitialTerm(matched)).with(result));
            }

        }
    }

    // =================================================================================================================
    // Optional and Repeatable cases
    // =================================================================================================================

    private static abstract class OptionalCase implements XmlCase {

        protected final Case<XmlTerm> content;

        public OptionalCase(Case<XmlTerm> content) {
            this.content = content;
        }

        @Override
        public Option<MatchResult> unapply(XmlTerm nodeList) {
            final XmlTerm secondary = nodeList.secondary();
            final MatchResult matchResult = unapplyXmlTerm(secondary);

            final List<Node> matched = new ArrayList<>();
            while (nodeList.size() > secondary.size()) {
                matched.add(nodeList.next());
            }

            if (nodeList.isInitial() && nodeList.hasNext()) {
                return new Option.None<>();
            } else {
                return new Option.Some<>(new MatchResult(new InitialTerm(matched)).with(matchResult));
            }
        }

        protected abstract MatchResult unapplyXmlTerm(XmlTerm xmlTerm);
    }

    private static class Opt extends OptionalCase {

        private Opt(Case<XmlTerm> content) {
            super(content);
        }

        @Override
        protected MatchResult unapplyXmlTerm(XmlTerm nodeList) {
            final MatchResult matchResult = new MatchResult(null);

            final Option<MatchResult> unapply = content.unapply(nodeList);

            if (!unapply.isNone()) {
                matchResult.with(unapply.value());
            }

            return matchResult;
        }
    }

    // =================================================================================================================

    private static class OptRep extends OptionalCase {

        private OptRep(Case<XmlTerm> content) {
            super(content);
        }

        @Override
        protected MatchResult unapplyXmlTerm(XmlTerm nodeList) {
            final MatchResult matchResult = new MatchResult(null);

            while (true) {
                final Option<MatchResult> unapply = content.unapply(nodeList);
                if (!unapply.isNone()) {
                    matchResult.with(unapply.value());
                } else {
                    break;
                }
            }

            return matchResult;
        }
    }

    // =================================================================================================================

    public static abstract class XmlTerm implements Iterator<Node> {
        protected final List<Node> nodes;

        {
            this.nodes = new ArrayList<>();
        }

        protected XmlTerm(Node node) {
            this.nodes.add(node);
        }

        protected XmlTerm(NodeList list) {
            for (int i = 0; i < list.getLength(); i += 1) {
                this.nodes.add(list.item(i));
            }
        }

        protected XmlTerm(List<Node> nodes) {
            this.nodes.addAll(nodes);
        }

        @Override
        public boolean hasNext() {
            return !this.nodes.isEmpty();
        }

        @Override
        public Node next() {
            return this.nodes.remove(0);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        public int size() {
            return this.nodes.size();
        }

        abstract boolean isInitial();

        abstract XmlTerm secondary();
    }

    // =================================================================================================================
    // XmlTerm specifications
    // =================================================================================================================

    private static class InitialTerm extends XmlTerm {
        private InitialTerm(Node node) {
            super(node);
        }

        private InitialTerm(NodeList list) {
            super(list);
        }

        private InitialTerm(List<Node> nodes) {
            super(nodes);
        }

        @Override
        boolean isInitial() {
            return true;
        }

        XmlTerm secondary() {
            return new SecondaryTerm(nodes);
        }
    }

    // =================================================================================================================

    private static class SecondaryTerm extends XmlTerm {
        protected SecondaryTerm(List<Node> list) {
            super(list);
        }

        @Override
        boolean isInitial() {
            return false;
        }

        XmlTerm secondary() {
            return new SecondaryTerm(this.nodes);
        }
    }
}
