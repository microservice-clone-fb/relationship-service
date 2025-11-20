package com.tam.relationship.service;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestCancelRequest;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestRespondRequest;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestSendRequest;
import com.tam.relationship.dto.request.relationshipuser.UnfriendRequest;
import com.tam.relationship.dto.request.relationshipuser.socket.SocketFriendRequestCancelPayload;
import com.tam.relationship.dto.request.relationshipuser.socket.SocketFriendRequestRespondPayload;
import com.tam.relationship.dto.request.relationshipuser.socket.SocketFriendRequestSendPayload;
import com.tam.relationship.dto.request.relationshipuser.socket.SocketFriendshipRemovePayload;
import com.tam.relationship.dto.response.SocketAckResponse;
import com.tam.relationship.exception.AppException;
import com.tam.relationship.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelationshipSocketEventHandler {

    private final SocketIOServer socketIOServer;
    private final UserService userService;

    private static final String EVENT_SEND_REQUEST = "friendRequest:send";
    private static final String EVENT_CANCEL_REQUEST = "friendRequest:cancel";
    private static final String EVENT_ACCEPT_REQUEST = "friendRequest:accept";
    private static final String EVENT_REJECT_REQUEST = "friendRequest:reject";
    private static final String EVENT_UNFRIEND = "friendship:remove";

    public RelationshipSocketEventHandler(SocketIOServer socketIOServer, UserService userService) {
        this.socketIOServer = socketIOServer;
        this.userService = userService;
    }

    @PostConstruct
    void registerEventListeners() {
        log.info("🔌 Registering Socket.IO relationship event listeners");
        socketIOServer.addEventListener(
                EVENT_SEND_REQUEST, SocketFriendRequestSendPayload.class, this::handleSendFriendRequest);
        socketIOServer.addEventListener(
                EVENT_CANCEL_REQUEST, SocketFriendRequestCancelPayload.class, this::handleCancelFriendRequest);
        socketIOServer.addEventListener(
                EVENT_ACCEPT_REQUEST, SocketFriendRequestRespondPayload.class, this::handleAcceptFriendRequest);
        socketIOServer.addEventListener(
                EVENT_REJECT_REQUEST, SocketFriendRequestRespondPayload.class, this::handleRejectFriendRequest);
        socketIOServer.addEventListener(EVENT_UNFRIEND, SocketFriendshipRemovePayload.class, this::handleUnfriend);
    }

    private void handleSendFriendRequest(
            SocketIOClient client, SocketFriendRequestSendPayload payload, AckRequest ackRequest) {
        respondWithAck(EVENT_SEND_REQUEST, ackRequest, () -> {
            String requesterId = requireUserId(client);
            if (payload == null || !StringUtils.hasText(payload.getTargetUserId())) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            FriendRequestSendRequest request = FriendRequestSendRequest.builder()
                    .requesterId(requesterId)
                    .targetUserId(payload.getTargetUserId())
                    .message(payload.getMessage())
                    .build();

            userService.sendFriendRequest(request);
            return SocketAckResponse.success("Friend request sent successfully");
        });
    }

    private void handleCancelFriendRequest(
            SocketIOClient client, SocketFriendRequestCancelPayload payload, AckRequest ackRequest) {
        respondWithAck(EVENT_CANCEL_REQUEST, ackRequest, () -> {
            String requesterId = requireUserId(client);
            if (payload == null || !StringUtils.hasText(payload.getTargetUserId())) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            FriendRequestCancelRequest request = FriendRequestCancelRequest.builder()
                    .requesterId(requesterId)
                    .targetUserId(payload.getTargetUserId())
                    .build();

            userService.cancelFriendRequest(request);
            return SocketAckResponse.success("Friend request cancelled successfully");
        });
    }

    private void handleAcceptFriendRequest(
            SocketIOClient client, SocketFriendRequestRespondPayload payload, AckRequest ackRequest) {
        respondWithAck(EVENT_ACCEPT_REQUEST, ackRequest, () -> {
            String targetUserId = requireUserId(client);
            if (payload == null || !StringUtils.hasText(payload.getRequesterId())) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            FriendRequestRespondRequest request = FriendRequestRespondRequest.builder()
                    .requesterId(payload.getRequesterId())
                    .targetUserId(targetUserId)
                    .decision(com.tam.relationship.dto.request.relationshipuser.FriendRequestDecision.ACCEPT)
                    .build();

            userService.respondFriendRequest(request);
            return SocketAckResponse.success("Friend request accepted");
        });
    }

    private void handleRejectFriendRequest(
            SocketIOClient client, SocketFriendRequestRespondPayload payload, AckRequest ackRequest) {
        respondWithAck(EVENT_REJECT_REQUEST, ackRequest, () -> {
            String targetUserId = requireUserId(client);
            if (payload == null || !StringUtils.hasText(payload.getRequesterId())) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            FriendRequestRespondRequest request = FriendRequestRespondRequest.builder()
                    .requesterId(payload.getRequesterId())
                    .targetUserId(targetUserId)
                    .decision(com.tam.relationship.dto.request.relationshipuser.FriendRequestDecision.REJECT)
                    .build();

            userService.respondFriendRequest(request);
            return SocketAckResponse.success("Friend request rejected");
        });
    }

    private void handleUnfriend(SocketIOClient client, SocketFriendshipRemovePayload payload, AckRequest ackRequest) {
        respondWithAck(EVENT_UNFRIEND, ackRequest, () -> {
            String userId = requireUserId(client);
            if (payload == null || !StringUtils.hasText(payload.getFriendId())) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            UnfriendRequest request = UnfriendRequest.builder()
                    .userId(userId)
                    .friendId(payload.getFriendId())
                    .build();

            userService.unfriend(request);
            return SocketAckResponse.success("Friend removed successfully");
        });
    }

    private void respondWithAck(String eventName, AckRequest ackRequest, AckSupplier supplier) {
        SocketAckResponse response;
        try {
            response = supplier.get();
        } catch (AppException ex) {
            log.warn("⚠️ Socket event '{}' failed: {}", eventName, ex.getMessage());
            response = SocketAckResponse.failure(ex.getErrorCode());
        } catch (Exception ex) {
            log.error("❌ Unexpected error while handling socket event '{}'", eventName, ex);
            response = SocketAckResponse.failure(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        if (ackRequest != null) {
            ackRequest.sendAckData(response);
        }
    }

    private String requireUserId(SocketIOClient client) {
        Object userIdAttr = client.get("userId");
        String userId = userIdAttr instanceof String ? (String) userIdAttr : null;
        if (!StringUtils.hasText(userId)) {
            userId = client.getHandshakeData().getSingleUrlParam("userId");
        }
        if (!StringUtils.hasText(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return userId;
    }

    @FunctionalInterface
    private interface AckSupplier {
        SocketAckResponse get();
    }
}
