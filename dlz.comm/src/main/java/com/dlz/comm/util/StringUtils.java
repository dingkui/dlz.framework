package com.dlz.comm.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringUtils {
    private static Pattern paraPattern = Pattern.compile("\\$\\{([\\w\\.]+)\\}");

    /**
     * 根据属性名称获得对应值
     *
     * \
     * @param name 属性名称
     * @param nullType 数据为null时类型  0返回null,1返回name,其他返回 {name}
     * @return 属性对应的值
     */
    public static Object getReplaceStr(String name, Function<String,Object> c,int nullType) {
        Object ret=c.apply(name);
        if(ret == null){
            return nullType==0?null:nullType==1?name:"{"+name+"}";
        }
        if(ret instanceof CharSequence){
            String retStr = ret.toString().trim();
            Matcher mat = paraPattern.matcher(retStr);
            StringBuilder sb=null;
            int end=0;
            while(mat.find()){
                String group = mat.group(1);
                if(sb==null){
                    sb=new StringBuilder();
                }
                sb.append(retStr, 0, mat.start());
                sb.append(getReplaceStr(group, c, 2));
                end=mat.end();
            }
            if(end==0){
                return retStr;
            }
            sb.append(retStr.substring(end));
            return sb.toString();
        }
        return ret;
    }

    public static String join(CharSequence separator, Iterable<?> strings) {
        Iterator<?> i = strings.iterator();
        if (!i.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i.next().toString());
        while (i.hasNext()) {
            sb.append(separator);
            sb.append(i.next().toString());
        }
        return sb.toString();
    }

    public static String NVL(String cs) {
        return NVL(cs, "");
    }

    public static String NVL(String cs, String defaultStr) {
        return cs == null ? defaultStr : cs;
    }

    private static Pattern myMsgPattern = Pattern.compile("\\{([^\\{\\}]*)\\}");

    public static String getBeanId(String className) {
        int lastIndexOf = className.lastIndexOf(".");
        return className.substring(lastIndexOf + 1, lastIndexOf + 2).toLowerCase() + className.substring(lastIndexOf + 2);
    }

    public static String getBeanId(Class<?> clazz) {
        return getBeanId(clazz.getName());
    }

    public static String formatMsg(Object message, Object... paras) {
        String msg = ValUtil.getStr(message, "");
        if (paras == null) {
            paras = new Object[]{null};
        }
        Matcher mat = myMsgPattern.matcher(msg);
        StringBuffer sb = new StringBuffer();
        int end = 0;
        int i = 0;
        while (mat.find()) {
            String indexStr = mat.group(1).replaceAll("[^\\d]*", "");
            int index = 0;
            if (!"".equals(indexStr)) {
                if (indexStr.length() > 2) {
                    index = -1;
                } else {
                    index = Integer.parseInt(indexStr);
                }
            } else {
                index = i;
            }
            sb.append(msg, end, mat.start());
            if (index > -1 && paras.length > index) {
                sb.append(ValUtil.getStr(paras[index], null));
            } else {
                sb.append(mat.group(0));
            }
            end = mat.end();
            i++;
        }
        return sb.append(msg, end, msg.length()).toString();
    }

    /**
     * 补0成指定长度的字符串
     *
     * @param i
     * @param length
     * @return
     */
    public static String addZeroBefor(long i, int length) {
        return leftPad(String.valueOf(i),length,'0');
    }

    /**
     * <p>
     * 检验是否为空
     * </p>
     *
     * <pre>
     * Collection，Map，Array,CharSequence
     * </pre>
     */
    @SuppressWarnings({"rawtypes"})
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        return false;
    }

    public static boolean isAnyEmpty(Object... cs) {
        for (final Object c : cs) {
            if (isEmpty(c)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断输入的字符是否为空或纯空格
     * <pre class="code">
     * StringUtils.isBlank(null) = true
     * StringUtils.isBlank("") = true
     * StringUtils.isBlank(" ") = true
     * StringUtils.isBlank("12345") = false
     * StringUtils.isBlank(" 12345 ") = false
     * </pre>
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnyBlank(CharSequence... cs) {
        for (final CharSequence c : cs) {
            if (isBlank(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllBlank(CharSequence... cs) {
        for (final CharSequence c : cs) {
            if (!isBlank(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean startsWithAny(final CharSequence sequence, final CharSequence... searchStrings) {
        if (isEmpty(sequence) || searchStrings.length==0) {
            return false;
        }
        for (final CharSequence searchString : searchStrings) {
            if (startsWith(sequence, searchString)) {
                return true;
            }
        }
        return false;
    }

    public static boolean startsWith(final CharSequence sequence, CharSequence searchString) {
        int length = searchString.length();
        if(isEmpty(sequence) || sequence.length()< length){
            return false;
        }
        for (int i = 0; i < length; i++) {
            if(sequence.charAt(i) != searchString.charAt(i)){
                return false;
            }
        }
        return true;
    }



    public static boolean isNumber(CharSequence o) {
        return o.length() > 0 && o.toString().replaceAll("[\\d.+-]", "").length() > 0;
    }

    public static boolean isLongOrInt(CharSequence o) {
        return o.length() > 0 && o.toString().replaceAll("[\\d+-]", "").length() > 0;
    }

    public static boolean isNotEmpty(Object cs) {
        return !isEmpty(cs);
    }

    public static String joinObject(Object cs, Object b) {
        return String.valueOf(cs) + String.valueOf(b);
    }

    /**
     * <p>
     * 首字母大写
     * </p>
     *
     * <pre>
     * StringUtils.capitalize(null)  = null
     * StringUtils.capitalize("")    = ""
     * StringUtils.capitalize("cat") = "Cat"
     * StringUtils.capitalize("cAt") = "CAt"
     * </pre>
     */
    public static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (Character.isTitleCase(firstChar)) {
            // already capitalized
            return str;
        }
        return new StringBuilder(strLen).append(Character.toTitleCase(firstChar)).append(str.substring(1)).toString();
    }

    /**
     * <p>
     * 在左边添加指定字符达到指定的长度
     * </p>
     *
     * <pre>
     * StringUtils.leftPad(null, *, *)     = null
     * StringUtils.leftPad("", 3, 'z')     = "zzz"
     * StringUtils.leftPad("bat", 3, 'z')  = "bat"
     * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
     * StringUtils.leftPad("bat", 1, 'z')  = "bat"
     * StringUtils.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     */
    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        return repeat(padChar, pads).concat(str);
    }

    /**
     * <p>
     * 构造指定个数的字符的字符串
     * </p>
     *
     * <pre>
     * StringUtils.repeat('e', 0)  = ""
     * StringUtils.repeat('e', 3)  = "eee"
     * StringUtils.repeat('e', -2) = ""
     * </pre>
     */
    public static String repeat(final char ch, int repeat) {
        final char[] buf = new char[repeat];
        while (repeat-->0) {
            buf[repeat] = ch;
        }
        return new String(buf);
    }

    /**
     * 下划线转驼峰 aa_bb_cc→aaBbCc
     * @param key
     * @return
     * @author dk 2015-04-09
     */
    public static String underScoreToCamel(String key) {
        if (key == null) {
            return "";
        }
        key = key.toLowerCase();
        Matcher mat = Pattern.compile("_([a-z])").matcher(key);
        while (mat.find()) {
            key = key.replace("_" + mat.group(1), mat.group(1).toUpperCase());
        }
        return key.replaceAll("_", "");
    }

    /**
     * 驼峰转下划线 aaBbCc→aa_bb_cc<br>
     * 如果参数含有_则不做转换
     * @param key
     * @author dk 2015-04-10
     * @return
     */
    public static String camelToUnderScore(String key) {
        if(key==null){
            return null;
        }
        if(key.indexOf("_")>-1){
            return key;
        }
        return key.replaceAll("([A-Z])", "_$1").toLowerCase(Locale.ROOT);
    }

     public static void main(String[] args) {
         log.debug(repeat('2',3));
         log.debug(underScoreToCamel("aa_bb_cc"));
         log.debug(camelToUnderScore("A_CC_VV_c"));
         log.debug(camelToUnderScore("aaBbCc"));
         log.debug("{}",isLongOrInt("-111"));
         log.debug("{}",isLongOrInt("1111"));
         log.debug("{}",isLongOrInt("+111.11"));
         log.debug("{}",ValUtil.getFloat("-111.11"));

         System.out.println(startsWith(new StringBuilder("123"),new StringBuilder("12")));
     }

    /**
     * <p>
     * 试用指定字符构造字符串
     * </p>
     *
     * <pre>
     * StringUtils.join(null, *)         = null
     * StringUtils.join([], *)           = ""
     * StringUtils.join([null], *)       = ""
     * StringUtils.join([1, 2, 3], ";")  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
     * </pre>
     */
    public static <T> String join(final T[] array, String separator) {
        if (array == null) {
            return null;
        }
        separator = NVL(separator);
        final int noOfItems = array.length;
        if (noOfItems <= 0) {
            return "";
        }
        final StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = 0; i < noOfItems; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            buf.append(array[i]);
        }
        return buf.toString();
    }

    public static <T> List<T> arrayToList(final T[] array) {
        return Arrays.asList(array);
    }

    public static <T> Object[] listToArray(final Collection<T> list) {
        return list.toArray();
    }

    public static <T> String join(final Collection<T> array, String separator) {
        return join(listToArray(array), separator);
    }

    public static <T> String join(final Collection<T> array, char separator) {
        return join(listToArray(array), String.valueOf(separator));
    }

    public static String[] split(String value, String regex) {
        return value == null ? null : value.split(regex);
    }

}
