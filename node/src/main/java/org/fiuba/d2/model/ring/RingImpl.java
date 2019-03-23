package org.fiuba.d2.model.ring;

import org.fiuba.d2.model.node.Token;
import org.fiuba.d2.model.node.Node;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.fiuba.d2.model.node.Token.TokenBuilder.createMaximum;
import static org.fiuba.d2.model.node.Token.TokenBuilder.createMinimum;

public class RingImpl implements Ring {

    private List<Range> ranges;
    private Node localNode;

    public RingImpl(Node node) {
        this.ranges = new ArrayList<>();
        this.localNode = node;
        initializeRing(node, node.getTokens().stream().sorted().collect(toList()));
    }

    @Override
    public void addNode(Node node) {
         node.getTokens().stream().sorted().forEach(token -> addNode(node, token));
    }

    @Override
    public void removeNode(Node node) {
        List<Range> ranges = this.ranges.stream()
                .filter(range -> !range.getNode().equals(node))
                .collect(toList());
        List<Range> newRing = new ArrayList<>();
        Token prev = createMinimum();
        Token next;
        for (Range range : ranges) {
            next = range.getTo();
            newRing.add(new Range(prev, next, range.getNode()));
            prev = next;
        }
        Range lastRange = newRing.get(newRing.size() - 1);
        newRing.remove(newRing.size() - 1);
        newRing.add(new Range(lastRange.getFrom(), createMaximum(), lastRange.getNode()));
        this.ranges = newRing;
    }

    @Override
    public Node getCoordinatorNode(Token token) {
        return ranges.stream()
                .filter(range -> range.contains(token))
                .findFirst()
                .map(Range::getNode)
                .orElse(null);
    }

    @Override
    public List<Range> getRanges(Node node) {
        return node.getTokens().stream()
                .map(token -> ranges.stream().filter(range -> range.contains(token)).collect(toList()))
                .flatMap(List::stream)
                .collect(toList());
    }

    @Override
    public Range getRange(Token token) {
        return ranges.stream()
                .filter(range -> range.contains(token))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Node> getNodes() {
        return ranges.stream().map(Range::getNode).distinct().collect(toList());
    }

    @Override
    public Node getLocalNode() {
        return localNode;
    }

    @Override
    public Node getNodeById(String id) {
        return ranges.stream()
                .map(Range::getNode)
                .filter(node -> node.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void initializeRing(Node node, List<Token> tokens) {
        Token prev = createMinimum();
        Token next;
        for (Token token : tokens) {
            next = token;
            ranges.add(new Range(prev, next, node));
            prev = next;
        }
        this.ranges.add(new Range(prev, createMaximum(), node));
    }

    private void addNode(Node node, Token token) {
        List<Range> newRing = new ArrayList<>();
        for (Range range : ranges) {
            if (range.contains(token)) {
                newRing.add(new Range(range.getFrom(), token, node));
                newRing.add(new Range(token, range.getTo(), range.getNode()));
            } else {
                newRing.add(range);
            }
        }
        ranges = newRing;
    }

}
