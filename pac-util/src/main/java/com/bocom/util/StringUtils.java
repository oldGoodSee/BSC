package com.bocom.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @说明 勇子sama的牛逼工具类
 * @工具包含范围 字符串
 */
public class StringUtils {
    
    private StringUtils() {

    }

    public static boolean isNullOrEmpty(String toTest) {
        return toTest == null || toTest.isEmpty();
    }

    public static boolean isNullOrEmpty(String... toTest) {
        for (String str : toTest) {
            if (str == null || str.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumber(String toTest) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(toTest);
        return isNum.matches();
    }

    public static boolean isNumber(String... toTest) {
        for (String str : toTest) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (!isNum.matches()) {
                return isNum.matches();
            }
        }
        return true;
    }

    /***
     * 判断 String 是否是 int
     *
     * @param input
     * @return
     */
    public static boolean isInteger(String input){
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
        return mer.find();
    }
}
