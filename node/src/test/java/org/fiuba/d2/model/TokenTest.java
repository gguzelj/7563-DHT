package org.fiuba.d2.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.fiuba.d2.model.Token.TokenBuilder.withValue;
import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void sortProperly() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(withValue(3));
        tokens.add(withValue(15));
        tokens.add(withValue(1));
        tokens.add(withValue(23));
        tokens.add(withValue(7));
        List<Token> sorted = tokens.stream().sorted().collect(toList());

        assertEquals(withValue(1), sorted.get(0));
        assertEquals(withValue(3), sorted.get(1));
        assertEquals(withValue(23), sorted.get(sorted.size()-1));
    }
}
