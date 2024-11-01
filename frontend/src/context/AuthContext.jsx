// src/context/AuthContext.js
import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authToken, setAuthToken] = useState(localStorage.getItem('authToken') || null);
    const [user, setUser] = useState(null);

    const login = (token, userData) => {
        setAuthToken(token);
        setUser(userData);
        localStorage.setItem('authToken', token);
    };

    const logout = () => {
        setAuthToken(null);
        setUser(null);
        localStorage.removeItem('authToken');
    };

    return (
        <AuthContext.Provider value={{ authToken, user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

// コンテキストを使いやすくするためのカスタムフック
export const useAuth = () => useContext(AuthContext);