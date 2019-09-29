package com.qclibrary.lib.utils;


public class QcStringUtils {

    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0 || "null".equals(s.toString());
    }

    public static boolean isEmpty(CharSequence... s) {
        for (CharSequence sss : s) {
            if (sss == null || sss.length() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b){
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean equalsNoEmpty(CharSequence a, CharSequence b) {
        if(isEmpty(a) || isEmpty(b)){
            return false;
        }else{
            return equals(a,b);
        }
    }

    public static boolean contains(String a, String b){
        if(isEmpty(a) || isEmpty(b)){
            return false;
        }else{
            return a.contains(b);
        }
    }

    public static boolean containsIgnoreCase(String a, String b){
        if(isEmpty(a) || isEmpty(b)){
            return false;
        }else{
            return a.toLowerCase().contains(b.toLowerCase());
        }
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        if(a == null || b == null){
            return false;
        }
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    public static String getMaxValue(int maxSize,String value){
        if(maxSize < value.length()){
            return value.substring(0,maxSize);
        }else{
            return value;
        }
    }
}