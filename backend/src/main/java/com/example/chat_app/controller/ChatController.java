package com.example.chat_app.controller;

import com.example.chat_app.model.Message;
import com.example.chat_app.model.User;
import com.example.chat_app.service.ChatService;
import com.example.chat_app.service.UserService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
    // メッセージをデータベースに保存
    Message savedMessage = chatService.saveMessage(message.getUserId(), message.getMessages(), message.getChatroomId());

    // 保存したメッセージをフロントエンドに送信
    //messagingTemplate.convertAndSend("/topic/messages", savedMessage);
    return savedMessage; // 受信したメッセージをそのまま返す（ブロードキャスト）

    }

    // 現在のユーザー情報を取得するエンドポイント
    @GetMapping("/api/current-user")
    @ResponseBody
    public User getCurrentUser() {
        // 現在ログイン中のユーザーを取得するロジックを実装
        User currentUser = userService.getCurrentUser(); // 現在のユーザーを取得するメソッドを呼び出す
        return currentUser; // ユーザー情報を返す
    }
}