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

package smallibs.suitcase.cases.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import smallibs.suitcase.cases.Case;
import smallibs.suitcase.cases.MatchResult;
import smallibs.suitcase.cases.core.Cases;
import smallibs.suitcase.cases.core.Var;
import smallibs.suitcase.utils.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Dom {

    public static Case<XmlTerm> Tag(Object name, final Object... content) {
        final List<Case<Element>> attributes = new ArrayList<>();
        final List<Object> newContent = new ArrayList<Object>() {{
            this.addAll(Arrays.asList(content));
        }};

        for (Object obj : content) {
            if (Att.isAnAttributeCase(obj)) {
                newContent.remove(0);
                attributes.add((Case<Element>) obj);
            } else {
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

    public static Case<XmlTerm> Empty = new Seq();

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
        public Option<MatchResult> unapply(XmlTerm nodeList) {
            final Option<MatchResult> result;

            if (checkNodeAvailability(nodeList)) {
                final Node node = nodeList.secondary().next();

                result = unapplyNode(node);

                if (!result.isNone()) {
                    nodeList.next();
                }
            } else {
                result = Option.None();
            }

            return result;
        }

        abstract Option<MatchResult> unapplyNode(Node node);
    }

    // =================================================================================================================

    private static class Att implements Case<Element> {

        private final Case<String> nameCase;
        private final Case<String> valueCase;

        Att(Object name, Object value) {
            this.nameCase = Cases.fromObject(name);
            this.valueCase = Cases.fromObject(value);
        }

        static boolean isAnAttributeCase(Object object) {
            if (object instanceof Var) {
                return isAnAttributeCase(((Var) object).getValue());
            } else {
                return object instanceof Att;
            }
        }

        @Override
        public Option<MatchResult> unapply(Element term) {
            for (int i = 0; i < term.getAttributes().getLength(); i += 1) {
                final Node node = term.getAttributes().item(i);
                final Option<MatchResult> unapplyName = nameCase.unapply(node.getNodeName());
                if (!unapplyName.isNone()) {
                    final Option<MatchResult> unapplyValue = valueCase.unapply(node.getNodeValue());
                    if (!unapplyValue.isNone()) {
                        return Option.Some(new MatchResult(node).with(unapplyName.value()).with(unapplyValue.value()));
                    }
                }
            }

            return Option.None();
        }

        @Override
        public List<Class> variableTypes() {
            final List<Class> classes = nameCase.variableTypes();
            classes.addAll(valueCase.variableTypes());
            return classes;
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
        Option<MatchResult> unapplyNode(Node node) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) node;
                final Option<MatchResult> unapplyName = name.unapply(element.getTagName());
                if (unapplyName.isNone()) {
                    return unapplyName;
                } else {
                    for (Case<Element> attribute : attributes) {
                        final Option<MatchResult> unapply = attribute.unapply(element);
                        if (unapply.isNone()) {
                            return unapply;
                        } else {
                            unapplyName.value().with(unapply.value());
                        }
                    }

                    final Option<MatchResult> unapplyContent = content.unapply(toXmlTerm(node.getChildNodes()));
                    if (unapplyContent.isNone()) {
                        return unapplyContent;
                    } else {
                        return Option.Some(new MatchResult(new InitialTerm(node)).with(unapplyName.value()).with(unapplyContent.value()));
                    }
                }
            } else {
                return Option.None();
            }
        }


        @Override
        public List<Class> variableTypes() {
            final List<Class> classes = name.variableTypes();

            for (Case<Element> attribute : attributes) {
                classes.addAll(attribute.variableTypes());
            }

            classes.addAll(content.variableTypes());

            return classes;
        }

    }

    // =================================================================================================================

    private static class Text extends NodeCase {

        private final Case<String> text;

        Text(Object text) {
            this.text = Cases.fromObject(text);
        }

        @Override
        Option<MatchResult> unapplyNode(Node node) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                final Option<MatchResult> unapplyText = text.unapply(node.getTextContent().trim());
                if (unapplyText.isNone()) {
                    return unapplyText;
                } else {
                    return Option.Some(new MatchResult(new InitialTerm(node)).with(unapplyText.value()));
                }
            } else {
                return Option.None();
            }
        }

        @Override
        public List<Class> variableTypes() {
            return text.variableTypes();
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
                    unapply = Option.None();
                }

                if (unapply.isNone()) {
                    return unapply;
                } else {
                    result.with(unapply.value());
                }
            }

            if (nodeList.isInitial() && secondary.hasNext()) {
                return Option.None();
            } else {
                final List<Node> matched = new ArrayList<>();
                while (nodeList.size() > secondary.size()) {
                    matched.add(nodeList.next());
                }
                return Option.Some(new MatchResult(new InitialTerm(matched)).with(result));
            }

        }


        @Override
        public List<Class> variableTypes() {
            final List<Class> classes = new ArrayList<>();

            for (Case<?> aCase : this.content) {
                classes.addAll(aCase.variableTypes());
            }

            return classes;
        }
    }

    // =================================================================================================================
    // Optional and Repeatable cases
    // =================================================================================================================

    private static abstract class Optional implements XmlCase {

        protected final Case<XmlTerm> content;

        Optional(Case<XmlTerm> content) {
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
                return Option.None();
            } else {
                return Option.Some(new MatchResult(new InitialTerm(matched)).with(matchResult));
            }
        }

        protected abstract MatchResult unapplyXmlTerm(XmlTerm xmlTerm);

        @Override
        public List<Class> variableTypes() {
            return content.variableTypes();
        }
    }

    private static class Opt extends Optional {

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

    private static class OptionalRepeatable extends Optional {

        OptionalRepeatable(Case<XmlTerm> content) {
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

        public final List<Node> nodes() {
            return Collections.unmodifiableList(this.nodes);
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
