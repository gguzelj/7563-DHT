package org.fiuba.d2.model;

import org.fiuba.d2.model.node.Token;
import org.fiuba.d2.model.ring.Range;
import org.junit.Test;

import java.math.BigInteger;

import static org.fiuba.d2.model.node.Token.TokenBuilder.createMaximum;
import static org.fiuba.d2.model.node.Token.TokenBuilder.createRandom;
import static org.fiuba.d2.model.node.Token.TokenBuilder.withValue;
import static org.junit.Assert.assertTrue;

public class RangeTest {

    @Test
    public void should_contain_random_token() {
        Range range = new Range(Token.TokenBuilder.createMinimum(), createMaximum(), null);
        assertTrue(range.contains(createRandom()));
    }

    @Test
    public void should_contain_token() {
        Range range = new Range(withValue(0), withValue(10), null);
        assertTrue(range.contains(withValue(1)));
    }

}