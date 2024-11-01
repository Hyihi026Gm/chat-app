// Registration.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export const Registration = () => {
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [loginId, setLoginId] = useState('');
    const navigate = useNavigate();
    const [error, setError] = useState(''); // エラーメッセージの状態

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/register', {
                loginId: loginId,
                userName: userName,
                password: password,
            },{
                withCredentials: true  // Cookieを送信するために必要
            });
            console.log('Registration successful:', response.data);
            // 登録が成功したらログイン画面にリダイレクト
            navigate('/login');
        } catch (error) {
            console.error('Registration error:', error);
            setError('Registration failed. Please try again.'); // エラーメッセージを設定
        }
    };

    return (
        <div>
            <h2>新規登録</h2>
            <form onSubmit={handleSubmit}>
                <input 
                    type="text" 
                    value={loginId} 
                    onChange={(e) => setLoginId(e.target.value)} 
                    placeholder="LoginId"
                    required // 必須入力
                />
                <input 
                    type="text" 
                    value={userName} 
                    onChange={(e) => setUserName(e.target.value)} 
                    placeholder="UserName"
                    required // 必須入力 
                />
                <input 
                    type="password" 
                    value={password} 
                    onChange={(e) => setPassword(e.target.value)} 
                    placeholder="Password"
                    required // 必須入力
                />
                <button type="submit">登録</button>
                {error && <p style={{ color: 'red' }}>{error}</p>} {/* エラーメッセージを表示 */}
            </form>
        </div>
    );
};