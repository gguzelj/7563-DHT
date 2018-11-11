package org.fiuba.d2.converter;

import org.fiuba.d2.dto.Token;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TokenConverter {

    static List<Token> convert(List<org.fiuba.d2.model.node.Token> tokens) {
        return tokens.stream().map(TokenConverter::convert).collect(toList());
    }

    static Token convert(org.fiuba.d2.model.node.Token token) {
        return new Token(token.getValue());
    }
}
