package br.com.phsaraiva.Whatsapp_clone_api.model.message;

import br.com.phsaraiva.Whatsapp_clone_api.model.message.enun.MessageState;
import br.com.phsaraiva.Whatsapp_clone_api.model.message.enun.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private Long id;
    private String content;
    private MessageType type;
    private MessageState state;
    private String senderId;
    private String receiverId;
    private LocalDateTime createdAt;
    private  byte[] media;
}
