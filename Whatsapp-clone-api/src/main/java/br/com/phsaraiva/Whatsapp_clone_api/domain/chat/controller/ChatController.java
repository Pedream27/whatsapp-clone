package br.com.phsaraiva.Whatsapp_clone_api.domain.chat.controller;

import br.com.phsaraiva.Whatsapp_clone_api.domain.chat.ChatReesponse;
import br.com.phsaraiva.Whatsapp_clone_api.domain.chat.services.ChatServices;
import br.com.phsaraiva.Whatsapp_clone_api.domain.common.StringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatServices chatServices;

    @PostMapping
    public ResponseEntity<StringResponse>   createChat(
            @RequestParam(name = "sender-id") String senderId,
            @RequestParam(name = "receiver-id") String receiverId
    ){
        final String chatId = chatServices.createChat(senderId, receiverId);
        StringResponse response = StringResponse.builder().response(chatId).build();
        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<List<ChatReesponse>> getChatsByReceiver(Authentication authentication){
        return ResponseEntity.ok(chatServices.getChatsReceiverId(authentication));

    }
}
