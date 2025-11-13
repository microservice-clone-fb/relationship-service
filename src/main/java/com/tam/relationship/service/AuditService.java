package com.tam.relationship.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service xử lý audit logic
 */
@Service
public class AuditService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    /**
     * Lấy username của user hiện tại từ SecurityContext
     */
    public String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                    return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
                }
                return authentication.getName();
            }
        } catch (Exception e) {
            // Không có security context hoặc lỗi khác
        }
        return "SYSTEM";
    }

    /**
     * Tạo history entry
     */
    public String createHistoryEntry(String user, String action, Instant timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        String formattedTime = dateTime.format(FORMATTER);
        return String.format("[%s] %s by %s", formattedTime, action, user);
    }

    /**
     * Tạo history entry với message tùy chỉnh
     */
    public String createHistoryEntry(String user, String action, String message, Instant timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        String formattedTime = dateTime.format(FORMATTER);
        return String.format("[%s] %s: %s by %s", formattedTime, action, message, user);
    }
}
