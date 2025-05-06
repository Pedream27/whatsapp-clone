package br.com.phsaraiva.Whatsapp_clone_api.message;

import br.com.phsaraiva.Whatsapp_clone_api.chat.Chat;
import br.com.phsaraiva.Whatsapp_clone_api.chat.ChatRepository;
import br.com.phsaraiva.Whatsapp_clone_api.file.FileService;
import br.com.phsaraiva.Whatsapp_clone_api.file.FileUtils;
import br.com.phsaraiva.Whatsapp_clone_api.notification.Notification;
import br.com.phsaraiva.Whatsapp_clone_api.notification.NotificationType;
import br.com.phsaraiva.Whatsapp_clone_api.notification.NotificationService;
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
    private final NotificationService notificationService;

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
        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(messageRequest.getType())
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .recipientId(messageRequest.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getChatName(message.getSenderId()))
                .build();

        notificationService.sendNotification(message.getRecipientId(), notification);


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

        final String recipientId = getRecipientID(chat, authentication);

        messageRepository.setMessagesToSeenByChat(chat.getId(), MessageState.SEEN);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .senderId(getSenderId(chat, authentication))
                .recipientId(recipientId)
                .type(NotificationType.SEEN)
                .build();

        notificationService.sendNotification(recipientId, notification);


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

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .type(NotificationType.IMAGE)
                .messageType(MessageType.IMAGE)
                .senderId(senderId)
                .recipientId(recipientId)
                .media(FileUtils.readFileFromLocation(filePath))
                .build();

        notificationService.sendNotification(recipientId, notification);


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
