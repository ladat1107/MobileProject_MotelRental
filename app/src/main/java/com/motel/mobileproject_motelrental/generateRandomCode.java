package com.motel.mobileproject_motelrental;

import java.util.Random;

public class generateRandomCode {
    public static String RandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // Số ngẫu nhiên từ 0 đến 9
            code.append(digit);
        }
        return code.toString();
    }
}
