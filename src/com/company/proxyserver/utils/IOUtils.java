package com.company.proxyserver.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class IOUtils {

    public static byte[] toByteArray(InputStream is) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        try {
            int r;
            while ((r = is.read(buff)) > 0) {
                out.write(buff, 0, r);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return out.toByteArray();
    }

    public static String toString(InputStream is) {
        return new String(toByteArray(is), StandardCharsets.UTF_8);
    }
}
