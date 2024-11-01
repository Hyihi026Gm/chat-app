import React, { useEffect, useState } from 'react';
import './Login.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';

export const Login = () => {
    const [loginId, setLoginId] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth(); // AuthContextからlogin関数を取得
    const navigate = useNavigate();
    let activityTimeout;

    const handleSubmit = async (e) => {
        e.preventDefault(); // フォームのデフォルトの送信を防ぐ
        try {
            const response = await axios.post('http://localhost:8080/api/login', {
                loginId: loginId,
                password: password,
            },{
                withCredentials: true  // クッキーやセッション情報を送る場合
            });
            // トークンを localStorage に保存
            localStorage.setItem('authToken', response.data.token); // トークンを保存
            console.log("Auth Token:", response.data.token); // トークンの値を確認

            const { authToken, userId } = response.data;
            login(authToken, { userId }); // Contextにトークンとユーザー情報を保存
            // ログイン成功時にチャット画面に遷移
            navigate('/chat');
            // セッションを延長するために関数を呼び出す
            await extendSession(); 
        } catch (error) {
            setError('ログインに失敗しました。ユーザー名とパスワードを確認してください。');
            console.error('Login error:', error);
        }
        console.log('Login:', loginId, password);
    };

    const handleRegister = () => {
        navigate('/register'); // 新規登録画面に遷移
    };

    const extendSession = async () => {
        try {
            await axios.post("/api/extend-session",{}, {
                withCredentials: true // Cookieを送信するために必要
            });
            console.log("Session extended successfully."); // レスポンスを処理
        } catch (error) {
            console.error("Error extending session:", error);
    }
    };

    useEffect(() => {
        const resetTimer = () => {
            clearTimeout(activityTimeout);
            activityTimeout = setTimeout(() => {
                console.log("セッションを切断します");
            }, 300000);
        };

        document.addEventListener("click", resetTimer);
        document.addEventListener("keypress", resetTimer);
        resetTimer(); // 初期タイマーの設定

        return () => {
            document.removeEventListener("click", resetTimer);
            document.removeEventListener("keypress", resetTimer);
        };
    }, []); // 空の依存配列で初回マウント時のみ実行

    return(
        <div>
        <h2>ログイン</h2>
        <form onSubmit={handleSubmit}>
            <input 
                type="text" 
                value={loginId} 
                onChange={(e) => setLoginId(e.target.value)} 
                placeholder="LoginId" 
            />
            <input 
                type="password" 
                value={password} 
                onChange={(e) => setPassword(e.target.value)} 
                placeholder="Password" 
            />
            <button type="submit">ログイン</button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </form>
        {/* 新規登録画面に遷移するボタン */}
        <button type="button" onClick={() => navigate('/register')}>新規登録はこちら</button>
    </div>
    );
};
