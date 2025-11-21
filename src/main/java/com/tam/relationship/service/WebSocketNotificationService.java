package com.tam.relationship.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tam.relationship.configuration.SocketIOService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class WebSocketNotificationService {

    @Value("${app.services.gateway:http://localhost:5000}")
    String gatewayUrl;

    final SocketIOService socketIOService; // Fallback: vẫn giữ để dùng nếu gateway không available
    final RestTemplate restTemplate;

    /**
     * Emit friend request sent event to target user
     * Gọi Gateway Socket.IO endpoint để emit event
     */
    public void notifyFriendRequestSent(String targetUserId, String requesterId, String requesterName) {
        log.info("📤 [WebSocket] Emitting friendRequest:received to target user: {}", targetUserId);
        log.info("📤 [WebSocket] Requester ID: {}", requesterId);

        FriendRequestNotification notification = FriendRequestNotification.builder()
                .type("FRIEND_REQUEST_SENT")
                .requesterId(requesterId)
                .requesterName(requesterName)
                .targetUserId(targetUserId)
                .build();

        // Gọi Gateway Socket.IO endpoint để emit event
        try {
            String url = gatewayUrl + "/internal/socketio/emit";
            EmitEventRequest request = new EmitEventRequest(targetUserId, "friendRequest:received", notification);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<EmitEventRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            log.info("✅ [WebSocket] Event emitted via Gateway - Status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("❌ [WebSocket] Failed to emit via Gateway, falling back to local Socket.IO", e);
            // Fallback: emit trực tiếp nếu gateway không available
            socketIOService.emitToUser(targetUserId, "friendRequest:received", notification);
        }

        log.info("✅ [WebSocket] Event friendRequest:received emitted to user: {}", targetUserId);
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    private static class EmitEventRequest {
        private String userId;
        private String eventName;
        private Object data;
    }

    /**
     * Emit friend request cancelled event
     */
    public void notifyFriendRequestCancelled(String targetUserId, String requesterId) {
        log.info("❌ [WebSocket] Emitting friendRequest:cancelled to user: {}", targetUserId);
        FriendRequestNotification notification = FriendRequestNotification.builder()
                .type("FRIEND_REQUEST_CANCELLED")
                .requesterId(requesterId)
                .targetUserId(targetUserId)
                .build();

        socketIOService.emitToUser(targetUserId, "friendRequest:cancelled", notification);
    }

    /**
     * Emit friend request accepted event
     */
    public void notifyFriendRequestAccepted(String requesterId, String targetUserId) {
        log.info("✅ [WebSocket] Emitting friendRequest:accepted to users: {} and {}", requesterId, targetUserId);
        FriendRequestNotification notification = FriendRequestNotification.builder()
                .type("FRIEND_REQUEST_ACCEPTED")
                .requesterId(requesterId)
                .targetUserId(targetUserId)
                .build();

        // Notify both users
        socketIOService.emitToUser(requesterId, "friendRequest:accepted", notification);
        socketIOService.emitToUser(targetUserId, "friendRequest:accepted", notification);
    }

    /**
     * Emit friend request rejected event
     */
    public void notifyFriendRequestRejected(String requesterId, String targetUserId) {
        log.info("❌ [WebSocket] Emitting friendRequest:rejected to user: {}", requesterId);
        FriendRequestNotification notification = FriendRequestNotification.builder()
                .type("FRIEND_REQUEST_REJECTED")
                .requesterId(requesterId)
                .targetUserId(targetUserId)
                .build();

        socketIOService.emitToUser(requesterId, "friendRequest:rejected", notification);
    }

    /**
     * Emit friendship removed event
     */
    public void notifyFriendshipRemoved(String requesterId, String targetUserId) {
        log.info("🗑️ [WebSocket] Emitting friendship:removed to users: {} and {}", requesterId, targetUserId);
        FriendRequestNotification notification = FriendRequestNotification.builder()
                .type("FRIENDSHIP_REMOVED")
                .requesterId(requesterId)
                .targetUserId(targetUserId)
                .build();

        socketIOService.emitToUser(requesterId, "friendship:removed", notification);
        socketIOService.emitToUser(targetUserId, "friendship:removed", notification);
    }

    /**
     * Notification DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class FriendRequestNotification {
        private String type; // FRIEND_REQUEST_SENT, FRIEND_REQUEST_CANCELLED, etc.
        private String requesterId;
        private String requesterName;
        private String targetUserId;
    }
}
