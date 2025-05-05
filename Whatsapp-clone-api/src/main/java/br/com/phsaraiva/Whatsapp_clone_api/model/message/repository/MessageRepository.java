package br.com.phsaraiva.Whatsapp_clone_api.model.message.repository;

import br.com.phsaraiva.Whatsapp_clone_api.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
