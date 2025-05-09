package pl.coderslab.workshop7.user;

import org.apache.commons.lang3.RandomStringUtils;


public class Helpers {
    private static final String DOMAIN = "@example.com";

    public enum CredentialType {
        USERNAME, EMAIL
    }

    public static String generateCredentials(int length, CredentialType type) {
        String randomString = RandomStringUtils.randomAlphanumeric(length);

        if (type == CredentialType.USERNAME) {
            return randomString;
        } else {
            return randomString + DOMAIN;
        }

    }
}
