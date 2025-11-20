package com.tam.relationship.service;

import org.springframework.stereotype.Service;

import com.tam.relationship.configuration.SocketIOService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class WebSocketNotificationService {

    SocketIOService socketIOService;

    /**
     * Emit friend request sent event to target user
     */
    public void notifyFriendRequestSent(String targetUserId, String requesterId, String requesterName) {
        log.info("📤 [WebSocket] Emitting friendRequest:sent to user: {}", targetUserId);
        FriendRequestNotification notification = FriendRequestNotification.builder()
                .type("FRIEND_REQUEST_SENT")
                .requesterId(requesterId)
                .requesterName(requesterName)
                .targetUserId(targetUserId)
                .build();

        // Emit to specific user via Socket.IO
        socketIOService.emitToUser(targetUserId, "friendRequest:received", notification);
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
