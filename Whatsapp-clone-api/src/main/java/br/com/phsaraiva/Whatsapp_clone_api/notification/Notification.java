package br.com.phsaraiva.Whatsapp_clone_api.notification;

import br.com.phsaraiva.Whatsapp_clone_api.message.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    private String chatId;
    private String content;
    private String senderId;
    private String recipientId;
    private String chatName;
    private MessageType messageType;
    private NotificationType type;
    private byte[] media;
}
