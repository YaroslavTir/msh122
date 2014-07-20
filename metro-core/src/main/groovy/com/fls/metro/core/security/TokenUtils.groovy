package com.fls.metro.core.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.codec.Hex

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * User: NFadin
 * Date: 17.04.14
 * Time: 16:42
 */
class TokenUtils {

    static final String MAGIC_KEY = "obfuscate";

    static String createUserToken(UserDetails userDetails) {
        def expires = System.currentTimeMillis() + 1000L * 60 * 60;
        "${PrincipalType.USER.name()}:${userDetails.username}:$expires:${computeUserSignature(userDetails, expires)}";
    }

    static String createImToken(UserDetails userDetails) {
        "${PrincipalType.IM.name()}:${userDetails.username}:${computeImSignature(userDetails)}"
    }

    static String computeImSignature(UserDetails userDetails) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        new String(Hex.encode(digest.digest("${userDetails.username}:$MAGIC_KEY".bytes)));
    }

    static String computeUserSignature(UserDetails userDetails, long expires) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        new String(Hex.encode(digest.digest("${userDetails.username}:$expires:${userDetails.password}:$MAGIC_KEY".bytes)));
    }

    static PrincipalType getPrincipalTypeFromToken(String authToken) {
        if (!authToken) return null
        PrincipalType.valueOf(authToken.split(':')[0])
    }

    static String getPrincipalNameFromToken(String authToken) {
        if (!authToken) return null
        authToken.split(':')[1]
    }

    static Long getExpiresFromToken(String authToken) {
        if (!authToken) return null
        Long.parseLong(authToken.split(':')[2])
    }

    static String getSignatureFromToken(String authToken) {
        if (!authToken) return null
        authToken.split(':')[3]
    }

    static boolean validateToken(String authToken, UserDetails userDetails) {
        PrincipalType type = getPrincipalTypeFromToken(authToken)
        switch (type) {
            case PrincipalType.USER:
                return getSignatureFromToken(authToken) ==
                        computeUserSignature(userDetails, getExpiresFromToken(authToken))
            case PrincipalType.IM:
                return userDetails.username == getPrincipalNameFromToken(authToken);
        }
    }
}
