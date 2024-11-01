package com.example.chat_app.model;

public class LoginRequest {
    private int loginId;
    private String password;

    // ゲッターとセッター
    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}