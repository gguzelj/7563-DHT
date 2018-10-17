package org.fiuba.d2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(HashGenerator.class);

    public static byte[] sha1(String input) {
        return sha1(input.getBytes());
    }

    public static byte[] sha1(int value) {
        byte[] a = {(byte) value};
        return sha1(a);
    }

    private static byte[] sha1(byte[] bytes) {
        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
            return mDigest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Unexpected error occurred while hashing {}",bytes, e);
            return null;
        }
    }
}
