package pl.coderslab.workshop7.user;

import java.util.Random;

public class Helpers {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DOMAIN = "@example.com";
    private static final Random RANDOM = new Random();

    public enum CredentialType {
        USERNAME, EMAIL
    }

    public static String generateCredentials(int length, CredentialType type) {
        StringBuilder email = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            email.append(CHARACTERS.charAt(index));
        }

        if (type == CredentialType.USERNAME) {
            return email.toString();
        } else {
            email.append(DOMAIN);
            return email.toString();
        }

    }
}
