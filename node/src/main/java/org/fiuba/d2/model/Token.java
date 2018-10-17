package org.fiuba.d2.model;

import org.fiuba.d2.utils.HashGenerator;

import java.math.BigInteger;
import java.util.Random;

public class Token {

    private final BigInteger value;

    public Token(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    public static class TokenBuilder {

        private static Random random = new Random();

        public Token createRandom() {
            return new Token(new BigInteger(1, HashGenerator.sha1(random.nextInt())));
        }
    }

}
