package br.com.phsaraiva.Whatsapp_clone_api.model.message.mapper;

import br.com.phsaraiva.Whatsapp_clone_api.model.message.Message;
import br.com.phsaraiva.Whatsapp_clone_api.model.message.MessageResponse;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {


    public MessageResponse toMessageResponse(Message  message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .receiverId(message.getRecipientId())
                .type(message.getType())
                .state(message.getState())
                .createdAt(message.getCreatedDate())
                // todo leitor de arquivo de midia
                .build();

    }
}
