package com.example.chat_app.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int messageId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "messages", length = 200)
    private String messages;

    @Column(name = "send_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @Column(name = "chatroom_id")
    private int chatroomId;

    // コンストラクタ、ゲッター、セッター
    public Message() {}

    public Message(int userId, String messages, Date sendDate, int chatroomId) {
        this.userId = userId;
        this.messages = messages;
        this.sendDate = sendDate;
        this.chatroomId = chatroomId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }
}