package com.catholic.ac.kr.booking_platform.helper;

public class HelperUtils {
    public static String encodeEmail(String email) {
        StringBuilder rs = new StringBuilder();
        int domainIndex = email.indexOf("@");

        for (int i = 0; i < email.length(); i++) {
            if (i == 0 || i == domainIndex - 1 || i >= domainIndex) {
                rs.append(email.charAt(i));
            } else {
                rs.append("*");
            }
        }
        return rs.toString();
    }

    public static String encodePhone(String phone) {
        StringBuilder rs = new StringBuilder();

        for (int i = 0; i < phone.length(); i++) {
            if (i >= phone.length() - 4) {
                rs.append("*");
            } else
                rs.append(phone.charAt(i));
        }

        return rs.toString();
    }

    public static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    public static boolean isInvalidEmail(String email) {
        String normalizedEmail = normalizeEmail(email);
        return !normalizedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static String normalizeUsername(String username) {
        return username.trim();
    }

    public static String normalizePhone(String phone) {
        return phone.replaceAll("\\s+", "");
    }

    public static boolean isInvalidPhone(String phone) {
        String normalizedPhone = normalizePhone(phone);
        return !normalizedPhone.matches("^\\d{9,11}$");
    }

    public static String normalizePassword(String password) {
        return password.trim();
    }
}
