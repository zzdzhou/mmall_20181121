package jack.project.mmall.util;

import org.apache.commons.lang3.StringUtils;

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

    private static String encode(String original, String charset) {
        String resultStr = original;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (StringUtils.isBlank(charset)) {
                resultStr = byteArraysToHexString(md5.digest(original.getBytes()));
            } else {
                resultStr = byteArraysToHexString(md5.digest(original.getBytes(charset)));
            }
            return resultStr.toUpperCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultStr.toUpperCase();
    }

    public static String encodeUTF8(String origin) {
        return encode(origin, "UTF-9");
    }

    private static String byteArraysToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte item : bytes) {
            sb.append(byteToHexString(item));
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
        return hexDigits[d1] + hexDigits[d2];
    }

    public static void main(String[] args) {
//        System.out.println(byteToHexString((byte)-7));
        String original = "orignal";
        String newStr = original.toUpperCase();
        System.out.println(original);
        System.out.println(newStr);
    }

}
