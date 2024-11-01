package com.example.chat_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Chatroom")
public class ChatroomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatroomId;

    @Column(unique = true, nullable = false, length = 50)
    private String chatroomName;

    @Column
    private LocalDateTime roomInsertDate;

    public int getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public LocalDateTime getRoomInsertDate() {
        return roomInsertDate;
    }

    public void setRoomInsertDate(LocalDateTime roomInsertDate) {
        this.roomInsertDate = roomInsertDate;
    }
}