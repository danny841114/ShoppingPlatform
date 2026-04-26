package com.danny.shoppingplatform.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageHelper {
    public static byte[] convertImageToByte(MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            return image.getBytes();
        }

        return null;
    }
}
