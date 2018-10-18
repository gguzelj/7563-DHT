package org.fiuba.d2.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.fiuba.d2.model.Token.TokenBuilder.createMaximum;
import static org.fiuba.d2.model.Token.TokenBuilder.createMinimum;

public class Ring {

    private List<Range> ranges;

    public Ring(Node node, List<Token> tokens) {
        this.ranges = new ArrayList<>();
        initializeRing(node, tokens.stream().sorted().collect(toList()));
    }

    public void addNode(Node node, List<Token> tokens) {
         tokens.stream().sorted().forEach(token -> addNode(node, token));
    }

    public void removeNode(Node node) {
        List<Range> collect = ranges.stream()
                .filter(range -> !range.getNode().equals(node))
                .collect(toList());
        List<Range> newRing = new ArrayList<>();
        Token prev = createMinimum();
        Token next;
        for (int i = 0; i < collect.size(); i++) {
            next = collect.get(i).getTo();
            newRing.add(new Range(prev, next, collect.get(i).getNode()));
            prev = next;
        }
        Range lastRange = newRing.get(newRing.size() - 1);
        newRing.remove(newRing.size() - 1);
        newRing.add(new Range(lastRange.getFrom(), createMaximum(), lastRange.getNode()));
        ranges = newRing;
    }

    public Node getCoordinatorNode(Token token) {
        return ranges.stream()
                .filter(range -> range.contains(token))
                .findFirst()
                .map(Range::getNode)
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
