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
import smallibs.suitcase.annotations.CaseType;
import smallibs.suitcase.pattern.Case;
import smallibs.suitcase.pattern.Cases;
import smallibs.suitcase.pattern.MatchResult;
import smallibs.suitcase.utils.Option;

import java.util.ArrayList;
import java.util.List;

public class Xml {


    public static Case<NodeList> Tag(Object name, Object... content) {
        return new Tag(name, Seq(content));
    }

    public static Case<NodeList> OptRep(Object... content) {
        return new OptRep(Seq(content));
    }

    public static Case<NodeList> Opt(Object... content) {
        return new Opt(Seq(content));
    }

    public static Case<NodeList> Rep(Object... content) {
        return new Rep(Seq(content));
    }

    public static Case<NodeList> Seq(Object... content) {
        return new Seq(content);
    }

    public static Case<NodeList> Text(Object content) {
        return new Text(content);
    }

    // =================================================================================================================
    // Visited node list
    // =================================================================================================================

    public static NodeList toNodeList(Node node) {
        return new SingleNodeList(node);
    }

    // =================================================================================================================

    private static class SingleNodeList implements NodeList {
        private final Node node;

        private SingleNodeList(Node node) {
            this.node = node;
        }

        @Override
        public Node item(int index) {
            if (index != 0) {
                return null;
            } else {
                return node;
            }
        }

        @Override
        public int getLength() {
            return 1;
        }
    }

    // =================================================================================================================

    private static class VisitedNodeList implements NodeList {
        private int offset;
        private final NodeList nodeList;

        private VisitedNodeList(NodeList nodeList) {
            this.offset = 0;
            this.nodeList = nodeList;
        }

        private boolean hasNextNode() {
            return getLength() > 0;
        }

        private int offset() {
            return this.offset;
        }

        private void reset(int offset) {
            this.offset = offset;
        }

        private void nextNode() {
            this.offset += 1;
        }

        private Node current() {
            return this.item(0);
        }

        @Override
        public Node item(int index) {
            return this.nodeList.item(index + this.offset);
        }

        @Override
        public int getLength() {
            return this.nodeList.getLength() - this.offset;
        }
    }

    // =================================================================================================================

    private static VisitedNodeList getVisitedNodeList(NodeList nodeList) {
        if (nodeList instanceof VisitedNodeList) {
            return (VisitedNodeList) nodeList;
        } else {
            return new VisitedNodeList(nodeList);
        }
    }

    // =================================================================================================================
    // Cases
    // =================================================================================================================

    private static abstract class NodeListCase implements Case<NodeList> {
        @Override
        public Option<MatchResult> unapply(NodeList nodeList) {
            final VisitedNodeList visitedNodeList = getVisitedNodeList(nodeList);
            final Option<MatchResult> unapply = unapplyVisitedNodeList(visitedNodeList);

            if (!unapply.isNone() && visitedNodeList != nodeList && visitedNodeList.getLength() > 0) {
                return new Option.None<>();
            } else {
                return unapply;
            }
        }

        protected abstract Option<MatchResult> unapplyVisitedNodeList(VisitedNodeList visitedNodeList);
    }

    // =================================================================================================================

    @CaseType(NodeList.class)
    private static class Seq extends NodeListCase {
        private final List<Case<NodeList>> contentCases;

        private Seq(Object... content) {
            this.contentCases = new ArrayList<>();
            for (Object aContent : content) {
                final Case<NodeList> aCase = Cases.fromObject(aContent);
                this.contentCases.add(aCase);
            }
        }

        protected Option<MatchResult> unapplyVisitedNodeList(VisitedNodeList visited) {
            final MatchResult matchResult = new MatchResult(visited);
            final int offset = visited.offset();

            for (int index = 0; visited.hasNextNode() && index < contentCases.size(); index += 1) {
                final int length = visited.getLength();
                final Case<NodeList> nodeListCase = contentCases.get(index);
                final Option<MatchResult> unapply = nodeListCase.unapply(visited);

                if (unapply.isNone()) {
                    visited.reset(offset);
                    return unapply;
                } else {
                    // Consume current node only if the term is not a dedicated Xml term
                    if (visited.getLength() == length && !(nodeListCase instanceof LambdaTransition)) {
                        visited.nextNode();
                    }
                    matchResult.with(unapply.value());
                }
            }

            return new Option.Some<>(matchResult);
        }
    }

    // =================================================================================================================

    private interface LambdaTransition {
    }

    // =================================================================================================================

    @CaseType(NodeList.class)
    private static class OptRep extends NodeListCase implements LambdaTransition {

        private final Case<NodeList> contentCases;

        private OptRep(Case<NodeList> content) {
            contentCases = content;
        }

        protected Option<MatchResult> unapplyVisitedNodeList(VisitedNodeList node) {
            final MatchResult matchResult = new MatchResult(node);

            Option<MatchResult> unapply = this.contentCases.unapply(node);

            while (!unapply.isNone() && node.hasNextNode()) {
                matchResult.with(unapply.value());
                unapply = this.contentCases.unapply(node);
            }

            if (!unapply.isNone()) {
                matchResult.with(unapply.value());
            }

            return new Option.Some<>(matchResult);
        }
    }

    // =================================================================================================================

    @CaseType(NodeList.class)
    private static class Rep extends NodeListCase {

        private final Case<NodeList> contentCases;

        private Rep(Case<NodeList> content) {
            contentCases = content;
        }

        protected Option<MatchResult> unapplyVisitedNodeList(VisitedNodeList node) {
            final MatchResult matchResult = new MatchResult(node);

            Option<MatchResult> unapply = this.contentCases.unapply(node);

            if (unapply.isNone()) {
                return new Option.None<>();
            }

            while (!unapply.isNone() && node.hasNextNode()) {
                matchResult.with(unapply.value());
                unapply = this.contentCases.unapply(node);
            }

            if (!unapply.isNone()) {
                matchResult.with(unapply.value());
            }

            return new Option.Some<>(matchResult);
        }
    }
    // =================================================================================================================

    @CaseType(NodeList.class)
    private static class Opt extends NodeListCase implements LambdaTransition {

        private final Case<NodeList> contentCases;

        private Opt(Case<NodeList> content) {
            contentCases = content;
        }

        protected Option<MatchResult> unapplyVisitedNodeList(VisitedNodeList node) {
            final MatchResult matchResult = new MatchResult(node);

            final Option<MatchResult> unapply = this.contentCases.unapply(node);

            if (!unapply.isNone()) {
                matchResult.with(unapply.value());
            }

            return new Option.Some<>(matchResult);
        }
    }

    // =================================================================================================================

    private static abstract class NodeCase implements Case<NodeList> {
        @Override
        public Option<MatchResult> unapply(NodeList nodeList) {
            final VisitedNodeList visitedNodeList = getVisitedNodeList(nodeList);

            if (visitedNodeList != nodeList && visitedNodeList.getLength() > 1) {
                return new Option.None<>();
            }

            if (visitedNodeList.hasNextNode()) {
                return unapplyNode(visitedNodeList.current());
            } else {
                return new Option.None<>();
            }
        }

        protected abstract Option<MatchResult> unapplyNode(Node current);
    }

    // =================================================================================================================

    @CaseType(NodeList.class)
    private static class Tag extends NodeCase {

        private final Case<String> nameCase;
        private final Case<NodeList> contentCase;

        private Tag(Object object, Object content) {
            this.nameCase = Cases.fromObject(object);
            this.contentCase = Cases.fromObject(content);
        }

        protected Option<MatchResult> unapplyNode(Node node) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) node;
                final Option<MatchResult> unapplyName = nameCase.unapply(element.getTagName());
                if (!unapplyName.isNone()) {
                    final Option<MatchResult> unapplyContent = contentCase.unapply(element.getChildNodes());
                    if (!unapplyContent.isNone()) {
                        return new Option.Some<>(new MatchResult(node).with(unapplyName.value()).with(unapplyContent.value()));
                    }
                }
            }
            return new Option.None<>();
        }
    }

    // =================================================================================================================

    @CaseType(NodeList.class)
    private static class Text extends NodeCase {

        private final Case<String> contentCase;

        private Text(Object content) {
            this.contentCase = Cases.fromObject(content);
        }

        protected Option<MatchResult> unapplyNode(Node node) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                return contentCase.unapply(node.getTextContent().trim());
            } else {
                return new Option.None<>();
            }
        }
    }
}
