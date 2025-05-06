package br.com.phsaraiva.Whatsapp_clone_api.model.message.controller;

import br.com.phsaraiva.Whatsapp_clone_api.model.message.MessageRequest;
import br.com.phsaraiva.Whatsapp_clone_api.model.message.MessageResponse;
import br.com.phsaraiva.Whatsapp_clone_api.model.message.repository.MessageRepository;
import br.com.phsaraiva.Whatsapp_clone_api.model.message.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage(@RequestBody MessageRequest message) {
        messageService.saveMessage(message);
    }

    @PostMapping(value = "/upload-media", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadMedia(
            @RequestParam("chat-id" ) String chatId,
            // todo add @Parameter from swagger
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        messageService.uploadMediaMessage(chatId, file, authentication);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setMessagesToSeeen(@RequestParam("chat-id") String chatId, Authentication authentication) {
        messageService.setMessageToSeen(chatId, authentication);
    }

    @GetMapping("/chat/{chat-id}")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable("chat-id") String chatId
    ){
        return ResponseEntity.ok(messageService.findChatMessages(chatId));
    }


}
