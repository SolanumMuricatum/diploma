import React, { useState } from 'react';
import { toast } from 'react-toastify';
import '../styles/login.css';
import { useAuth } from '../auth/AuthProvider';

export const Login = () => {
  const [activeTab, setActiveTab] = useState('login');
  const [user, setUser] = useState({login: '', email: '', password: ''});

  const { login } = useAuth();

  const handleTabClick = (tab) => {
    setUser({login: '', email: '', password: ''});
    setActiveTab(tab);
  };

  const postUser = async () => {
    try {
      const data = await apiFetch(`http://localhost:8080/user/save`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          login: user.login,
          email: user.email,
          password: user.password,
        }),
      });

      toast.success('Пользователь успешно зарегистрирован!', {
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

/*   const login = async () => {
    try {
      console.log(user.login);
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
  }; */

  async function apiFetch(url, options = {}) {
    try {
      const response = await fetch(url, options);
      const data = await response.json().catch(() => ({}));

      if (!response.ok) {
        throw new Error(data.error || `Ошибка ${response.status}`);
      }

      return data; 

    } catch (error) {
      if (error instanceof TypeError) {
        throw new Error('Сервер недоступен. Попробуйте позже.');
      }
      throw error;
    }
  }



  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setUser(prevAnswers => ({
      ...prevAnswers,
      [name]: value,
    }));
  };

  return (
    <div className="container">
      <div className="tabs">
          {activeTab === 'login' ? 'Вход' : 'Регистрация'}
      </div>

      {activeTab === 'login' && (
        <form className="tab-content" onSubmit={(e) => { e.preventDefault(); login(user); }}>
          
          <input className="input" name="login" value={user.login} type="text" placeholder="Логин" onChange={handleInputChange} required/>
          <input className="input" name="password" value={user.password} type="password" placeholder="Пароль" onChange={handleInputChange} required/>

          <div className="options">
            <a href="#!">Забыли пароль?</a>
          </div>

          <button className="btn" type="submit">Войти</button>
          <p className="text-center">
            Не зарегистрированы? <a href="#!" onClick={() => handleTabClick('register')}>Зарегистрироваться</a>
          </p>
        </form>
      )}

      {activeTab === 'register' && (
        <form className="tab-content" onSubmit={(e) => { e.preventDefault(); postUser(); }}>

          <input className="input" name='login' value={user.login} type="text" placeholder="Логин" onChange={handleInputChange} required/>
          <input className="input" name='email' value={user.email} type="email" placeholder="Электронная почта" onChange={handleInputChange} required/>
          <input className="input" name='password' value={user.password} type="password" placeholder="Пароль" onChange={handleInputChange} required/>

          <button className="btn" type="submit">Зарегистрироваться</button>
          <p className="text-center">
            Уже есть аккаунт? <a href="#!" onClick={() => handleTabClick('login')}>Войти</a>
          </p>
        </form>
      )}
    </div>
  );
};
