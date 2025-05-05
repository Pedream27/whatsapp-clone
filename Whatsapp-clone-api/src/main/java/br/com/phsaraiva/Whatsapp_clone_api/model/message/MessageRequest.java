package br.com.phsaraiva.Whatsapp_clone_api.model.message;

import br.com.phsaraiva.Whatsapp_clone_api.model.message.enun.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {

    private String content;
    private String senderId;
    private String receiverId;
    private MessageType type;
    private String chatId;
}
