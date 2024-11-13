package com.craftilio.account_service.utils;
import java.util.Random;

public class AppUtils {
    public static String generateAccountNumber() {
        var random = new Random();
        long accountNumber = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        return String.valueOf(accountNumber);
    }
}
