package br.com.phsaraiva.Whatsapp_clone_api.model.message;

import br.com.phsaraiva.Whatsapp_clone_api.common.BaseAuditingEntity;
import br.com.phsaraiva.Whatsapp_clone_api.model.chat.Chat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.print.attribute.standard.MediaSize;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
@NamedQuery(name =  MessageContants.FIND_MESSAGES_BY_CHAT_ID ,
query =  "SELECT  m FROM  Message  m  WHERE  m.chat.id = :chatId ORDER BY  m.createdDate")
public class Message extends BaseAuditingEntity {
    @Id
    @SequenceGenerator(name = "msg_seq", sequenceName = "msg_seq" , allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msg_seq" )
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageState state;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "sender_id", nullable = false)
    private String senderId;
    @Column(name = "recipient_id", nullable = false)
    private String recipientId;




}
