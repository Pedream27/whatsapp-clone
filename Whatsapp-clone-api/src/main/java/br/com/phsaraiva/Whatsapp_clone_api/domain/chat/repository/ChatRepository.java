package br.com.phsaraiva.Whatsapp_clone_api.domain.chat.repository;

import br.com.phsaraiva.Whatsapp_clone_api.domain.chat.Chat;
import br.com.phsaraiva.Whatsapp_clone_api.domain.chat.ChatContants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, String> {

    @Query(name = ChatContants.FIND_CHAT_BY_SENDER_ID)
    List<Chat> findChatBySenderId(@Param("senderId") String userId);
    @Query(name = ChatContants.FIND_CHAT_BY_SENDER_ID_AND_RECIVER)
    Optional<Chat> findChatBySenderIdAndReciver(@Param("senderId") String senderId, @Param("recipientId") String receiverId);
}
