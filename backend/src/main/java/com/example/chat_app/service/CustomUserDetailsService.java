package com.example.chat_app.service;

import com.example.chat_app.model.User;
import com.example.chat_app.repository.UserRepository;
import com.example.chat_app.security.CustomUserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        int loginId = Integer.parseInt(username); // usernameをint型に変換
        User user = userRepository.findByLoginId(loginId);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        
        //Collection<GrantedAuthority> authorities = getAuthorities(user);
        return new CustomUserDetails(  // 直接CustomUserDetailsを返す
        user.getUserId(),
        user.getLoginId(),
        user.getPassword(),
        getAuthorities(user)// ユーザーの権限リストを取得
    );
    }

    private Collection<GrantedAuthority> getAuthorities(User user) {
        // 権限を取得し、SimpleGrantedAuthorityのリストを生成
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        // ユーザーに特定のロールがある場合の仮実装
        // user.getRoles() がロール名のリストを返すと仮定
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        return authorities;
    }
}