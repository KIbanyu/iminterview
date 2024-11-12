package com.craftilio.customer_service.utils;

import java.util.Random;

public class AppUtil {
    public static String generatePin(){
        Random random = new Random();
        return String.valueOf(1100 + random.nextInt(900));
    }
}
