package com.danny.shoppingplatform.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class NumberUtil {
    public static String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "ORD" + timestamp + randomNum;
    }
}
