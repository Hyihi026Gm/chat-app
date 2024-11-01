package com.example.chat_app.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.security.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.chat_app.model.LoginRequest;
import com.example.chat_app.model.RegistrationRequest;
import com.example.chat_app.model.User;
import com.example.chat_app.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
/* ↑Spring MVCのアノテーションで、RESTfulなAPIを構築するために使用。このクラスがREST APIのコントローラであることを示す。*/
@RequestMapping("/api")
/* ↑このコントローラが持つ全てのエンドポイントのベースURIを指定。ここでは/apiがベースURI。*/
@SessionAttributes("user")
public class UserController {

    @Autowired
    private UserService userService;

    @SuppressWarnings("deprecation")
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 推奨されるシークレットキー生成方法

    // ログインエンドポイント
    @PostMapping("/login")
    /* このメソッドがHTTP POSTリクエストに対して応答することを示す。エンドポイントは/api/login*/
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        // 1. ユーザー名とパスワードを取得
        int loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();

        // 2. ユーザーをデータベースから取得（ここでユーザーを検索する）
        User user = userService.findByLoginId(loginId);
        // 認証サービスを使用してユーザー認証
        boolean isAuthenticated = userService.authenticateUser(loginId, password);

        if (isAuthenticated) {
            // 認証が成功した場合、トークンを生成
        String token = generateToken(user); // ユーザーを基にトークンを生成
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("authToken", token);
        response.put("userId", user.getUserId());
        return ResponseEntity.ok(response);
        } else {
            // 認証失敗
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    // 新規登録エンドポイント
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {
        // 登録処理（ユーザー情報をデータベースに保存）
        if (registerUser(registrationRequest)) {
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.status(400).body("Registration failed");
        }
    }

    private boolean registerUser(RegistrationRequest registrationRequest) {
        // ユーザー登録処理を実装
        userService.registerUser(registrationRequest.getLoginId(), registrationRequest.getUserName(), registrationRequest.getPassword());
        return true;
    }

    @SuppressWarnings("deprecation")
    private String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 300000); // 有効期限を設定
    
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId())) // ユーザーID
                .setIssuedAt(now) // 発行日時
                .setExpiration(expirationDate) // 有効期限
                //.claim("role", user.getRole()) // ユーザーの役割（オプション）
                .signWith(SECRET_KEY) // シークレットキー
                .compact();
    }
}