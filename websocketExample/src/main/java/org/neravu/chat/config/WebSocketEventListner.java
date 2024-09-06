package org.neravu.chat.config;

import java.util.Objects;

import org.neravu.chat.dto.Message;
import org.neravu.chat.dto.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListner {
	
	@Autowired
	private final SimpMessageSendingOperations sendingOperations;
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String)headerAccessor.getSessionAttributes().get("username");
	    if(Objects.nonNull(username)) {
	    	log.info(" User Disconnected: {} " +username);
	    	sendingOperations.convertAndSend("/topic/chat",Message.builder().messageType(MessageType.LEAVE).sender(username).build());
	    }
	}

}
