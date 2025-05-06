package br.com.phsaraiva.Whatsapp_clone_api.domain.message.services;

import br.com.phsaraiva.Whatsapp_clone_api.domain.chat.Chat;
import br.com.phsaraiva.Whatsapp_clone_api.domain.chat.repository.ChatRepository;
import br.com.phsaraiva.Whatsapp_clone_api.domain.file.FileService;
import br.com.phsaraiva.Whatsapp_clone_api.domain.message.Message;
import br.com.phsaraiva.Whatsapp_clone_api.domain.message.MessageRequest;
import br.com.phsaraiva.Whatsapp_clone_api.domain.message.MessageResponse;
import br.com.phsaraiva.Whatsapp_clone_api.domain.message.enun.MessageState;
import br.com.phsaraiva.Whatsapp_clone_api.domain.message.enun.MessageType;
import br.com.phsaraiva.Whatsapp_clone_api.domain.message.mapper.MessageMapper;
import br.com.phsaraiva.Whatsapp_clone_api.domain.message.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final FileService fileService;
    private final MessageMapper mapper;

    public void saveMessage(MessageRequest messageRequest) {

        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(( ) -> new EntityNotFoundException("Chat not found"));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setRecipientId(messageRequest.getReceiverId());
        message.setType(messageRequest.getType());
        message.setState(MessageState.SENT);

        messageRepository.save(message);

        // todo notification

    }

    public List<MessageResponse> findChatMessages(String chatId){
        return  messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(mapper::toMessageResponse)
                .toList();

    }
    @Transactional
    public void setMessageToSeen(String chatId, Authentication authentication){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(( ) -> new EntityNotFoundException("Chat not found"));

       //  final String recipientId = getRecipientID(chat, authentication);

        messageRepository.setMessagesToSeenByChat(chat.getId(), MessageState.SEEN);

        // todo notification


    }

    public void uploadMediaMessage(String chatId, MultipartFile file , Authentication authentication)  {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(( ) -> new EntityNotFoundException("Chat not found"));

        final String senderId = getSenderId(chat, authentication);
        final String recipientId = getRecipientID(chat , authentication);

        final String filePath = fileService.saveFile(file , senderId);
        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setType(MessageType.IMAGE);
        message.setState(MessageState.SENT);
        message.setMediaFilePath(filePath);
        messageRepository.save(message);

        //  todo notification

    }





    private String getSenderId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getSender().getId();
        }
        return chat.getRecipient().getId();

    }

    private String getRecipientID(Chat chat, Authentication authentication) {
        if(chat.getSender().equals(authentication.getName())){
            return chat.getRecipient().getId();
        }
        return  chat.getSender().getId();
    }
}
