import React from 'react';
import { AuthProvider } from './context/AuthContext';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Navigate } from 'react-router-dom';
import {Login} from './components/Login/Login';
import {ChatWindow} from './components/Chat/ChatWindow';
import { Registration } from './components/Registration/Registration';

const App = () => {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          {/* "/login" にアクセスした時、Login コンポーネントを表示 */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Registration />} /> {/* 新規登録のルート */}
          {/* "/chat" にアクセスした時、ChatWindow コンポーネントを表示 */}
          <Route path="/chat" element={<ChatWindow />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;