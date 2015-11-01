/*
 * Copyright (C)2015 D. Plaindoux.
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

package org.smallibs.suitcase.cases.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.MatchResult;
import org.smallibs.suitcase.cases.core.Cases;
import org.smallibs.suitcase.cases.core.Var;
import java.util.Optional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Dom {

    public static Case<XmlTerm> Empty = new Seq();

    public static Case<XmlTerm> Tag(Object name, final Object... content) {
        final List<Case<Element>> attributes = new ArrayList<>();
        final List<Object> newContent = new ArrayList<Object>() {{
            this.addAll(Arrays.asList(content));
        }};

        for (Object obj : content) {
            final Boolean isAttribute = Att.getAttributeCase(obj).map(elementCase -> {
                newContent.remove(0);
                attributes.add(elementCase);
                return true;
            }).orElse(false);

            // All attributes are managed ... the remaining content is the tag body

            if (!isAttribute) {
                break;
            }
        }

        return new Tag(name, attributes, Seq(newContent.toArray()));
    }

    public static Case<Element> Att(Object name, Object value) {
        return new Att(name, value);
    }

    public static Case<XmlTerm> Text(Object content) {
        return new Text(content);
    }

    public static Case<XmlTerm> OptRep(Object... content) {
        return new OptionalRepeatable(Seq(content));
    }

    public static Case<XmlTerm> Opt(Object... content) {
        return new Opt(Seq(content));
    }

    public static Case<XmlTerm> Seq(Object... content) {
        return new Seq(content);
    }

    public static Case<XmlTerm> Rep(Object... content) {
        final Case<XmlTerm> seq = Seq(content);
        return Seq(seq, new OptionalRepeatable(seq));
    }

    // =================================================================================================================
    // Type "transformation" in order to transform a node or a node list to an xml term
    // =================================================================================================================

    public static XmlTerm toXmlTerm(Node node) {
        return new InitialTerm(node);
    }

    public static XmlTerm toXmlTerm(NodeList list) {
        return new InitialTerm(list);
    }

    // =================================================================================================================
    // General type fro Xml Case - Only used for specification purpose
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
        public Optional<MatchResult> unapply(XmlTerm nodeList) {
            final Optional<MatchResult> result;

            if (checkNodeAvailability(nodeList)) {
                final Node node = nodeList.secondary().next();

                result = unapplyNode(node);

                if (result.isPresent()) {
                    nodeList.next();
                }
            } else {
                result = Optional.empty();
            }

            return result;
        }

        abstract Optional<MatchResult> unapplyNode(Node node);
    }

    // =================================================================================================================

    private static class Att implements Case<Element> {

        private final Case<String> nameCase;
        private final Case<String> valueCase;

        Att(Object name, Object value) {
            this.nameCase = Cases.fromObject(name);
            this.valueCase = Cases.fromObject(value);
        }

        static Optional<Case<Element>> getAttributeCase(Object object) {
            if (object instanceof Var) {
                return getAttributeCase(((Var) object).getValue());
            } else if (object instanceof Att) {
                return Optional.of((Att)object);
            } else {
                return Optional.empty();
            }
        }

        @Override
        public Optional<MatchResult> unapply(Element term) {
            for (int i = 0; i < term.getAttributes().getLength(); i += 1) {
                final Node node = term.getAttributes().item(i);
                final Optional<MatchResult> unapplyName = nameCase.unapply(node.getNodeName());
                if (unapplyName.isPresent()) {
                    final Optional<MatchResult> unapplyValue = valueCase.unapply(node.getNodeValue());
                    if (unapplyValue.isPresent()) {
                        return Optional.ofNullable(new MatchResult(node).with(unapplyName.get()).with(unapplyValue.get()));
                    }
                }
            }

            return Optional.empty();
        }

        @Override
        public int variables() {
            return nameCase.variables() + valueCase.variables();
        }
    }

    // =================================================================================================================

    private static class Tag extends NodeCase {
        private final Case<String> name;
        private final List<Case<Element>> attributes;
        private final Case<XmlTerm> content;

        Tag(Object name, List<Case<Element>> attributes, Case<XmlTerm> content) {
            this.name = Cases.fromObject(name);
            this.attributes = attributes;
            this.content = content;
        }

        @Override
        Optional<MatchResult> unapplyNode(Node node) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) node;
                final Optional<MatchResult> unapplyName = name.unapply(element.getTagName());
                if (!unapplyName.isPresent()) {
                    return unapplyName;
                } else {
                    for (Case<Element> attribute : attributes) {
                        final Optional<MatchResult> unapply = attribute.unapply(element);
                        if (!unapply.isPresent()) {
                            return unapply;
                        } else {
                            unapplyName.get().with(unapply.get());
                        }
                    }

                    final Optional<MatchResult> unapplyContent = content.unapply(toXmlTerm(node.getChildNodes()));
                    if (!unapplyContent.isPresent()) {
                        return unapplyContent;
                    } else {
                        return Optional.ofNullable(new MatchResult(new InitialTerm(node)).with(unapplyName.get()).with(unapplyContent.get()));
                    }
                }
            } else {
                return Optional.empty();
            }
        }


        @Override
        public int variables() {
            int variables = 0;

            for (Case<Element> attribute : attributes) {
                variables += attribute.variables();
            }

            variables += content.variables();

            return variables;
        }

    }

    // =================================================================================================================

    private static class Text extends NodeCase {

        private final Case<String> text;

        Text(Object text) {
            this.text = Cases.fromObject(text);
        }

        @Override
        Optional<MatchResult> unapplyNode(Node node) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                final Optional<MatchResult> unapplyText = text.unapply(node.getTextContent().trim());
                if (!unapplyText.isPresent()) {
                    return unapplyText;
                } else {
                    return Optional.ofNullable(new MatchResult(new InitialTerm(node)).with(unapplyText.get()));
                }
            } else {
                return Optional.empty();
            }
        }

        @Override
        public int variables() {
            return text.variables();
        }
    }

    // =================================================================================================================
    // Sequence pattern
    // =================================================================================================================

    private static class Seq implements XmlCase {

        private List<Case<XmlTerm>> content;

        Seq(Object... content) {
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
        public Optional<MatchResult> unapply(XmlTerm nodeList) {
            final XmlTerm secondary = nodeList.secondary();
            final MatchResult result = new MatchResult(null);

            for (Case<XmlTerm> aContent : content) {
                final Optional<MatchResult> unapply;

                if (isXmlCase(aContent)) {
                    unapply = aContent.unapply(secondary);
                } else if (secondary.hasNext()) {
                    unapply = aContent.unapply(new InitialTerm(secondary.next()));
                } else {
                    unapply = Optional.empty();
                }

                if (!unapply.isPresent()) {
                    return unapply;
                } else {
                    result.with(unapply.get());
                }
            }

            if (nodeList.isInitial() && secondary.hasNext()) {
                return Optional.empty();
            } else {
                final List<Node> matched = new ArrayList<>();
                while (nodeList.size() > secondary.size()) {
                    matched.add(nodeList.next());
                }
                return Optional.ofNullable(new MatchResult(new InitialTerm(matched)).with(result));
            }

        }


        @Override
        public int variables() {
            int variables = 0;

            for (Case<?> aCase : this.content) {
                variables += aCase.variables();
            }

            return variables;
        }
    }

    // =================================================================================================================
    // Optional and Repeatable cases
    // =================================================================================================================

    private static abstract class OptionalCase implements XmlCase {

        protected final Case<XmlTerm> content;

        OptionalCase(Case<XmlTerm> content) {
            this.content = content;
        }

        @Override
        public Optional<MatchResult> unapply(XmlTerm nodeList) {
            final XmlTerm secondary = nodeList.secondary();
            final MatchResult matchResult = unapplyXmlTerm(secondary);

            final List<Node> matched = new ArrayList<>();
            while (nodeList.size() > secondary.size()) {
                matched.add(nodeList.next());
            }

            if (nodeList.isInitial() && nodeList.hasNext()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(new MatchResult(new InitialTerm(matched)).with(matchResult));
            }
        }

        protected abstract MatchResult unapplyXmlTerm(XmlTerm xmlTerm);

        @Override
        public int variables() {
            return content.variables();
        }
    }

    private static class Opt extends OptionalCase {

        private Opt(Case<XmlTerm> content) {
            super(content);
        }

        @Override
        protected MatchResult unapplyXmlTerm(XmlTerm nodeList) {
            final MatchResult matchResult = new MatchResult(null);

            final Optional<MatchResult> unapply = content.unapply(nodeList);

            if (unapply.isPresent()) {
                matchResult.with(unapply.get());
            }

            return matchResult;
        }
    }

    // =================================================================================================================

    private static class OptionalRepeatable extends OptionalCase {

        OptionalRepeatable(Case<XmlTerm> content) {
            super(content);
        }

        @Override
        protected MatchResult unapplyXmlTerm(XmlTerm nodeList) {
            final MatchResult matchResult = new MatchResult(null);

            while (true) {
                final Optional<MatchResult> unapply = content.unapply(nodeList);
                if (unapply.isPresent()) {
                    matchResult.with(unapply.get());
                } else {
                    break;
                }
            }

            return matchResult;
        }
    }

    // =================================================================================================================
    // XmlTerm specifications
    // =================================================================================================================

    public static abstract class XmlTerm {
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

        private XmlTerm(List<Node> nodes) {
            this.nodes.addAll(nodes);
        }

        private boolean hasNext() {
            return !this.nodes.isEmpty();
        }

        public Node next() {
            return this.nodes.remove(0);
        }

        public int size() {
            return this.nodes.size();
        }

        abstract protected boolean isInitial();

        protected XmlTerm secondary() {
            return new SecondaryTerm(this.nodes);
        }

    }

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
        protected boolean isInitial() {
            return true;
        }
    }

    // =================================================================================================================

    private static class SecondaryTerm extends XmlTerm {
        protected SecondaryTerm(List<Node> list) {
            super(list);
        }

        @Override
        protected boolean isInitial() {
            return false;
        }
    }

}
