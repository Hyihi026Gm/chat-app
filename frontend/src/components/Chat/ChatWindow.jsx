import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import './Chat.css';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';

let client = null;

export const ChatWindow = () => {
    const { authToken,user} = useAuth();
    //console.log("User from context:", user);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [userId, setUserId] = useState(null);
    const chatroomId = 1;
    const [isConnected, setIsConnected] = useState(false);
    

    useEffect(() => {
        // ユーザーIDを取得
        const fetchUserId = async () => {
            try {
                const response = await axios.get('/api/current-user', { withCredentials: true });
                setUserId(response.data.userId);

                const token = localStorage.getItem('authToken'); // トークンを取得
                if (!token) {
                    console.error("No auth token found."); // トークンがない場合のエラーメッセージ
                    return;
                }
                //console.log("Auth Token:", token); // トークンの値を確認
            
                // WebSocket接続の設定
                client = new Client({
                    // webSocketFactory: () => {
                    //     const sock = new SockJS(`http://localhost:8080/chat-websocket`);
                    //     sock.onopen = () => {
                    //         sock.send(JSON.stringify({ token: token })); // トークンを送信
                    //     };
                    //     return sock;
                    // },
                    webSocketFactory: () => new SockJS(`http://localhost:8080/chat-websocket?token=${token}`), // トークンをURLに追加
                    reconnectDelay: 5000,
                    onConnect: () => {
                        //console.log("Connected!");
                        //console.log(`Connecting to WebSocket: http://localhost:8080/chat-websocket?token=${token}`);
                        setIsConnected(true); // 接続成功時に状態を更新
                        // メッセージを購読し、受信したメッセージをリストに追加
                        client.subscribe('/topic/messages', message => {
                            const receivedMessage = JSON.parse(message.body);
                            console.log("Received message:", receivedMessage); // 受信したメッセージをコンソールに出力
                            setMessages(prevMessages => [...prevMessages, receivedMessage]);
                        });
                    },
                    onStompError: (error) => {
                        console.error("Error: ", error);
                    }
                });
                client.activate();
            } catch (error) {
                console.error("Error fetching user data:", error);
            }
        };

        fetchUserId();

        return () => {
            if (client) client.deactivate();
        };
    }, []);

    // メッセージ送信ハンドラ
    const sendMessage = () => {
        if (newMessage.trim() && client && isConnected && user?.userId) {
            const message = {
                userId: user?.userId, // userからuserIdを取得
                messages: newMessage, // メッセージコンテンツ
                chatroomId: chatroomId, // チャットルームIDを追加
            };
            console.log("Sending message:", message); 
            client.publish({ destination: "/app/send", body: JSON.stringify(message) });
            setNewMessage(''); // 入力欄をクリア
        }else {
            console.log("Message is empty or client is not connected."); // ここを追加
        }
    };

    return (
        <div>
            <h2>チャット</h2>
            <h3>ようこそ、ユーザーID: {user?.userId}</h3>
            <div className="chat-window">
                {messages.map((msg) => (
                    <div key={msg.messageId} className={`message ${msg.userId === user?.userId ? 'sent' : 'received'}`}>
                        <strong>{msg.userId === user?.userId ? 'ME' : 'YOU'}</strong>: {msg.messages}
                    </div>
                ))}
            </div>
            <div className="input-container">
                <input
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="メッセージを入力"
                />
                <button onClick={sendMessage}>送信</button>
            </div>
        </div>
    );
};
