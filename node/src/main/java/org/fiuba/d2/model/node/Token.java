package org.fiuba.d2.model.node;

import org.fiuba.d2.utils.HashGenerator;
import org.fiuba.d2.utils.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

@Embeddable
public class    Token implements Comparable<Token> {

    private String value;

    private Token() {
    }

    public Token(String value) {
        this.value = value;
    }

    public String getValue() {
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
            return new Token(StringUtils.getHexString(HashGenerator.sha1(random.nextInt())));
        }

        public static Token createMinimum() {
            byte[] a = new byte[20];
            for (int i = 0; i < 20; i++)
                a[i] = (byte)0x00;
            return new Token(StringUtils.getHexString(a));
        }

        public static Token createMaximum() {
            byte[] a = new byte[20];
            for (int i = 0; i < 20; i++)
                a[i] = (byte)0xFF;
            return new Token(StringUtils.getHexString(a));
        }

        public static Token withValue(int integer) {
            return new Token(StringUtils.getHexString(BigInteger.valueOf(integer).abs().toByteArray()));
        }

        public static Token withValue(byte[] bytes) {
            return new Token(StringUtils.getHexString(bytes));
        }

    }

}
