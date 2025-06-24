package com.stlang.chat_app.config;

import com.stlang.chat_app.ChatMessage;
import com.stlang.chat_app.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "WEB-SOCKET-EVENT-LISTENER")
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ) {
        // TODO: Implement code
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getSessionAttributes().get("username").toString();
        if(username != null) {
            log.info("User Disconnected: {} ", username);
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVER)
                    .sender(username)
                    .build();
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

}
