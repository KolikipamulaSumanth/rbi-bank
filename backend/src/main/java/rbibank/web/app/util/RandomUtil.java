package rbibank.web.app.util;

import java.security.SecureRandom;

public class RandomUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    public Long generateRandom(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = secureRandom.nextInt(10);
            if (i == 0 && digit == 0) {
                digit = 1;
            }
            sb.append(digit);
        }
        return Long.parseLong(sb.toString());
    }
}
