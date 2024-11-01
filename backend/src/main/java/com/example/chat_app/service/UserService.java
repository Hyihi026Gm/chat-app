package com.example.chat_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager; //認証を処理するためのインターフェース
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; //ユーザ名とパスワードを使用して認証するためのトークン
import org.springframework.security.core.Authentication; //認証の結果を表すインターフェース
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chat_app.model.User;
import com.example.chat_app.repository.UserRepository;
import com.example.chat_app.security.CustomUserDetails;


import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 新規登録
    @Transactional
    public void registerUser(int loginId, String userName, String rawPassword) {
        try {
        User user = new User();
        user.setLoginId(loginId);
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(rawPassword)); // パスワードを暗号化
        user.setSignupDate(LocalDateTime.now());

        //DBにユーザを保存
        userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 認証処理（パスワードの確認）
    public boolean authenticateUser(int loginId, String password) {
        try {
        // 認証処理
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginId, password)
            );

            // 認証が成功した場合、SecurityContextHolder に認証情報を設定
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(SecurityContextHolder.getContext().getAuthentication());
            return true;
        } catch (BadCredentialsException e) {
            // 認証失敗
            return false;
        }
    }

    // 現在のユーザー情報を取得するメソッド
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
        return null;  // 未認証の場合は null を返す、またはエラーハンドリングを行う
    }
    // 認証済みの場合、ユーザー情報を取得
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    return userRepository.findByLoginId(userDetails.getLoginId());
    }

    public User findByLoginId(int loginId) {
        return userRepository.findByLoginId(loginId); // リポジトリを使ってユーザーを検索
    }
}