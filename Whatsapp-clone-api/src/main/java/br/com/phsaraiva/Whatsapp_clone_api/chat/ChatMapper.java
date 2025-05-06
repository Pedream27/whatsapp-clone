package br.com.phsaraiva.Whatsapp_clone_api.chat;

import org.springframework.stereotype.Component;

@Component
public class ChatMapper {


    public ChatReesponse toChatResponse(Chat chat, String senderId) {
        return ChatReesponse.builder()
                .id(chat.getId())
                .name(chat.getChatName(senderId))
                .unreadCount(chat.getUnreadMessage(senderId))
                .lastMassage(chat.getLastMessage())
                .isRecipientOnline(chat.getRecipient().isUserOnLine())
                .senderId(chat.getSender().getId())
                .receiverId(chat.getRecipient().getId())
                .build();
    }

}
