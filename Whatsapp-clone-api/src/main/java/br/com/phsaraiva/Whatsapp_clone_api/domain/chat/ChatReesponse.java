package br.com.phsaraiva.Whatsapp_clone_api.domain.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatReesponse {

    private String id;
    private String name;
    private Long unreadCount;
    private String lastMassage;
    private LocalDateTime lastMassageTime;
    private boolean isRecipientOnline;
    private String senderId;
    private String  receiverId;
}
