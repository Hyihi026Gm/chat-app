package com.example.chat_app.service;

import com.example.chat_app.model.Message;
import com.example.chat_app.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ChatService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(int userId, String messageContent, int chatroomId) {
        Message message = new Message(userId, messageContent, new Date(), chatroomId);
        return messageRepository.save(message);
    }
}