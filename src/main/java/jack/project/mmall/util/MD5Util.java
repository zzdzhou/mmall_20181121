package jack.project.mmall.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-04
 */
public class MD5Util {

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public static String MD5Encode(String origin, String charset) {
        String resultStr = new String(origin);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (charset == null || "".equals(charset)) {
                resultStr = byteArraysToHexString(md5.digest(origin.getBytes()));
            } else {
                resultStr = byteArraysToHexString(md5.digest(origin.getBytes(charset)));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultStr.toUpperCase();
    }

    public static String MD5EncodeUtf(String origin) {
        return MD5Encode(origin, "UTF-8");
    }

    private static String byteArraysToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[2];
    }

}
