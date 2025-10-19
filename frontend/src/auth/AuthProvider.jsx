// src/context/AuthContext.js
import React from "react";
import { createContext, useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import { useNavigate } from 'react-router-dom'; 

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const navigate = useNavigate();
  const [userId, setUserId] = useState();
  const [loading, setLoading] = useState(true);

const checkAuth = async () => {
  setLoading(true);
  try {
    const res = await fetch("http://localhost:8080/auth/check", {
      method: "GET",
      credentials: "include",
    });

    if (!res.ok) throw new Error("Not authorized");

    const data = await res.json();
    setUserId(data);  
  } catch (err) {
    setUserId(null);
  } finally {
    setLoading(false);
  }
};


    // Проверка авторизации при первом рендере
  useEffect(() => {
    checkAuth();
  }, []);

  // Вызов при логине
  const login = async ( user ) => {
    try {
      const data = await apiFetch(`http://localhost:8080/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          login: user.login,
          password: user.password,
        }),
      });

      toast.success('Пользователь успешно вошёл в систему!', {
        onClose: () => {
          navigate(`/main`)
          window.location.reload();
        }
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
    setUserId(null); //вернуть когда допишу метод сервера
    toast.success('Вы вышли из аккаунта', {
      onClose: () => {
        navigate(`/`)
        window.location.reload();
      }
    });
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
