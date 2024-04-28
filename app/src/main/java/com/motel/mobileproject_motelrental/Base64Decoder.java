package com.motel.mobileproject_motelrental;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Decoder {
    public static String decodeBase64(String encodedString) {
        byte[] encodedBytes = encodedString.getBytes(StandardCharsets.UTF_8);
        byte[] decodedBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decodedBytes = Base64.getDecoder().decode(encodedBytes);
        }
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
