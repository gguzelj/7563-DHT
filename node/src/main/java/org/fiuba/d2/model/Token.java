package org.fiuba.d2.model;

import org.fiuba.d2.utils.HashGenerator;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

@Entity
public class Token implements Comparable<Token> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Lob
    private BigInteger value;

    private Token() {
    }

    public Token(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    @Override
    public int compareTo(Token o) {
        return value.compareTo(o.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public static class TokenBuilder {

        private static Random random = new Random();

        public static Token createRandom() {
            return new Token(new BigInteger(1, HashGenerator.sha1(random.nextInt())));
        }

        public static Token createMinimum() {
            byte[] a = new byte[20];
            for (int i = 0; i < 20; i++)
                a[i] = (byte)0x00;
            return new Token(new BigInteger(1, a));
        }

        public static Token createMaximum() {
            byte[] a = new byte[20];
            for (int i = 0; i < 20; i++)
                a[i] = (byte)0xFF;
            return new Token(new BigInteger(1, a));
        }

        public static Token withValue(int integer) {
            return new Token(BigInteger.valueOf(integer).abs());
        }

    }

}