// src/context/AuthContext.js
import React from "react";
import { createContext, useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [userId, setUserId] = useState(null);
  const [loading, setLoading] = useState(true);

  // Проверка авторизации при первом рендере
  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = () => {
    setLoading(true);
    fetch("http://localhost:8080/auth/check", {
      method: "GET",
      credentials: "include",
    })
      .then((res) => {
        if (res.ok) return res.json();
        throw new Error("Not authorized");
      })
      .then((data) => setUserId(data))
      .catch(() => setUserId(null))
      .finally(() => setLoading(false));
  };

  // Вызов при логине
  const login = async ( user ) => {
    try {
      const data = await apiFetch(`http://localhost:8080/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          login: user.login,
          password: user.password,
        }),
      });

      toast.success('Пользователь успешно вошёл в систему!', {
        onClose: () => window.location.reload()
      });
      console.log('Ответ сервера:', data);
      return data;

    } catch (error) {
      toast.error(error.message);
      console.error(error);
      return { error: error.message };
    }
  };

  async function apiFetch(url, options = {}) {
    try {
      const response = await fetch(url, options);
      const data = await response.json().catch(() => ({}));

      if (!response.ok) {
        throw new Error(data.message || `Ошибка ${response.status}`);
      }
      
      return data; 

    } catch (error) {
      setUserId(null);
      if (error instanceof TypeError) {
        throw new Error('Сервер недоступен. Попробуйте позже.');
      }
      throw error;
    }
  }

  // Вызов при логауте
  const logout = async () => {
    await fetch("http://localhost:8080/auth/logout", {
      method: "POST",
      credentials: "include",
    });
    setUserId(null);
  };

  return (
    <AuthContext.Provider value={{ userId, loading, login, logout, checkAuth }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
