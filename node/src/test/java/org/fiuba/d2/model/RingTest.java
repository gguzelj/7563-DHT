package org.fiuba.d2.model;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.fiuba.d2.model.Token.TokenBuilder.withValue;

public class RingTest {

    @Test
    public void addNode() {
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);

        Ring ring = new Ring(node1, createTokens(1, 10));
        ring.addNode(node2, createTokens(5, 15));

        Assert.assertEquals(node2, ring.getCoordinatorNode(withValue(2)));
        Assert.assertEquals(node1, ring.getCoordinatorNode(withValue(7)));
        Assert.assertEquals(node2, ring.getCoordinatorNode(withValue(11)));
        Assert.assertEquals(node1, ring.getCoordinatorNode(withValue(16)));
        Assert.assertEquals(node1, ring.getCoordinatorNode(withValue(1)));
        Assert.assertEquals(node2, ring.getCoordinatorNode(withValue(5)));
        Assert.assertEquals(node1, ring.getCoordinatorNode(withValue(10)));
    }

    @Test
    public void removeNode1() {
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        Node node3 = Mockito.mock(Node.class);

        Ring ring = new Ring(node1, createTokens(10));
        ring.addNode(node2, createTokens(20));
        ring.addNode(node3, createTokens(30));

        ring.removeNode(node1);
        ring.removeNode(node2);

        Assert.assertEquals(node3, ring.getCoordinatorNode(withValue(10)));
    }

    @Test
    public void removeNode2() {
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        Node node3 = Mockito.mock(Node.class);

        Ring ring = new Ring(node1, createTokens(10));
        ring.addNode(node2, createTokens(20));
        ring.addNode(node3, createTokens(30));

        ring.removeNode(node2);

        Assert.assertEquals(node3, ring.getCoordinatorNode(withValue(15)));
    }

    @Test
    public void removeNode3() {
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        Node node3 = Mockito.mock(Node.class);

        Ring ring = new Ring(node1, createTokens(10));
        ring.addNode(node2, createTokens(20));
        ring.addNode(node3, createTokens(30));

        ring.removeNode(node1);

        Assert.assertEquals(node2, ring.getCoordinatorNode(withValue(7)));
    }


    private List<Token> createTokens(Integer... values) {
        List<Token> response = new ArrayList<>();
        for (Integer value : values) {
            response.add(withValue(value));
        }
        return response;
    }
}
