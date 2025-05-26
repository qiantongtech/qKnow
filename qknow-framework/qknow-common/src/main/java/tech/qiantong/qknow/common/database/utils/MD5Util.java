package tech.qiantong.qknow.common.database.utils;

import tech.qiantong.qknow.common.utils.StringUtils;

import java.security.MessageDigest;
import java.util.Arrays;

public class MD5Util {

    public static void main(String[] args) throws InterruptedException {
        Object[] arr = new Object[]{"dbName"};
        Object[] objects = Arrays.copyOf(arr, arr.length + 2);
        System.out.println(objects.length);
        int length = arr.length;
        objects[length] = 1;
        objects[length+1] = 2;
        System.out.println(Arrays.toString(objects));
//        String encrypt = MD5Util.encrypt("sql" + ":" + Arrays.toString(arr));
//        System.out.println(encrypt);
    }

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5加密
     */
    public static String encrypt(String value){
        return encrypt(value.getBytes());
    }

    /**
     * MD5加密
     */
    public static String encrypt(byte[] value){
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(value);
            char[] chars = new char[32];
            for (int i = 0; i < chars.length; i = i + 2) {
                byte b = bytes[i / 2];
                chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
                chars[i + 1] = HEX_CHARS[b & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            throw new RuntimeException("md5 encrypt error", e);
        }
    }


    /**
     * 将字符串转换为 Long 类型。如果字符串为空或无法转换，则返回 0L。
     *
     * @param dataLength 要转换的字符串
     * @return 转换后的 Long 类型值
     */
    public static Long getStringToLong(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0L;
        }
        try {
            return Long.parseLong(dataLength);
        } catch (NumberFormatException e) {
            // 如果转换失败，则返回 0L
            return 0L;
        }
    }
    /**
     * 转义字符串中的单引号，避免拼接 SQL 时出错
     */
    public static String escapeSingleQuotes(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }

    /**
     * 如果输入的字符串全部为小写，则转换为大写返回，否则直接返回原字符串。
     *
     * @param input 输入字符串
     * @return 如果是全小写，返回全大写字符串；否则返回原字符串
     */
    public static String convertIfLowercase(String input) {
        if (input == null) {
            return null;
        }
        // 如果字符串与它的小写形式相同，说明全为小写
        if (input.equals(input.toLowerCase())) {
            return input.toUpperCase();
        }
        return input;
    }

}
