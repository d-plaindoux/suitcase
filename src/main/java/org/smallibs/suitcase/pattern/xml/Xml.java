package org.smallibs.suitcase.pattern.xml;

import org.smallibs.suitcase.annotations.CaseType;
import org.smallibs.suitcase.pattern.Cases;
import org.smallibs.suitcase.pattern.core.Case;
import org.smallibs.suitcase.utils.Option;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;

public final class Xml {

    public static Case<Element> Tag(Object name, Object content) {
        return new Tag(name, content);
    }

    public static Case<Node> Text(Object content) {
        return new Text(content);
    }

    @CaseType(Element.class)
    private static class Tag implements Case<Element> {

        private final Case<String> nameCase;
        private final Case<NodeList> contentCase;

        private Tag(Object object, Object content) {
            this.nameCase = Cases.fromObject(object);
            this.contentCase = Cases.fromObject(content);
        }

        @Override
        public int numberOfVariables() {
            return this.nameCase.numberOfVariables() + this.contentCase.numberOfVariables();
        }

        @Override
        public Option<List<Object>> unapply(Element element) {
            final Option<List<Object>> unapplyName = nameCase.unapply(element.getTagName());
            if (!unapplyName.isNone()) {
                final Option<List<Object>> unapplyContent = contentCase.unapply(element.getChildNodes());
                if (!unapplyContent.isNone()) {
                    final List<Object> objects = new LinkedList<>();
                    objects.addAll(unapplyName.value());
                    objects.addAll(unapplyContent.value());
                    return new Option.Some<>(objects);
                }
            }

            return new Option.None<>();
        }
    }

    @CaseType(NodeList.class)
    private static class Seq implements Case<NodeList> {

        private final List<Case<Node>> contentCases;

        private Seq(Object... content) {
            this.contentCases = new LinkedList<>();
            for (Object aContent : content) {
                final Case<Node> aCase = Cases.fromObject(aContent);
                this.contentCases.add(aCase);
            }
        }

        @Override
        public int numberOfVariables() {
            int numberOfVariables = 0;
            for (Case<Node> contentCase : contentCases) {
                numberOfVariables += contentCase.numberOfVariables();
            }
            return numberOfVariables;
        }

        @Override
        public Option<List<Object>> unapply(NodeList node) {

            return new Option.None<>();
        }
    }

    @CaseType(Node.class)
    private static class Text implements Case<Node> {

        private final Case<String> contentCase;

        private Text(Object content) {
            this.contentCase = Cases.fromObject(content);
        }

        @Override
        public int numberOfVariables() {
            return this.contentCase.numberOfVariables();
        }

        @Override
        public Option<List<Object>> unapply(Node node) {
            if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
                return contentCase.unapply(node.getTextContent());
            } else {
                return new Option.None<>();
            }
        }
    }
}
