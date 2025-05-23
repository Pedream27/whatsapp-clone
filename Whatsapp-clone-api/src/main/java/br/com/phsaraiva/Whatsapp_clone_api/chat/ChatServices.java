package br.com.phsaraiva.Whatsapp_clone_api.chat;

import br.com.phsaraiva.Whatsapp_clone_api.user.User;
import br.com.phsaraiva.Whatsapp_clone_api.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServices {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper mapper;


    @Transactional(readOnly = true)
    public List<ChatReesponse> getChatsReceiverId(Authentication currenUser){
        final String userId = currenUser.getName();
        return chatRepository.findChatBySenderId(userId)

                .stream()
                .map( c -> mapper.toChatResponse(c , userId))
                .toList();
    }

    public String createChat(String senderId, String receiverId) {
            Optional<Chat> existingChat = chatRepository.findChatBySenderIdAndReciver(senderId, receiverId);
            if (existingChat.isPresent()) {
               return existingChat.get().getId();
            }

            User sender = userRepository.findUserByPublicId(senderId)
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + senderId + " not found"));

        User receiver = userRepository.findUserByPublicId(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + receiverId + " not found"));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setRecipient(receiver);

        Chat savedChat = chatRepository.save(chat);
        return savedChat.getId();

    }
}
