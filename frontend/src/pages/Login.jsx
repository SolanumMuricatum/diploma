import React, { useState } from 'react';
import { toast } from 'react-toastify';
import '../styles/login.css';
import { useAuth } from '../auth/AuthProvider';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

export const Login = () => {
  const [activeTab, setActiveTab] = useState('login');
  const [user, setUser] = useState({ login: '', email: '', password: '' });
  const [isForgotModalOpen, setIsForgotModalOpen] = useState(false);
  const [resetEmail, setResetEmail] = useState("");
  const [isSending, setIsSending] = useState(false);
  const [isClosing, setIsClosing] = useState(false);

  const closeForgotModal = () => {
    setIsClosing(true);
    setTimeout(() => {
      setIsForgotModalOpen(false);
      setIsClosing(false);
      setResetEmail(""); // Очищаем поле при закрытии
    }, 300); // Время анимации из CSS
  };

  const { login } = useAuth();

  const handleTabClick = (tab) => {
    setUser({ login: '', email: '', password: '' });
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

      alert("Пользователь успешно зарегистрирован!");

      console.log('Ответ сервера:', data);
      return data;

    } catch (error) {
      alert(error.message);
      console.error(error);
      return { error: error.message };
    }
  };

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

  const handlePasswordReset = async (e) => {
    e.preventDefault();
    setIsSending(true);
    try {
      const res = await fetch(`http://localhost:8080/user/login/data/recovery?email=${resetEmail}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      if (res.ok) {
        alert("Инструкции по восстановлению отправлены на вашу почту!");
        setIsForgotModalOpen(false);
        setResetEmail("");
      } else {
        alert("Пользователь с такой почтой не найден.");
      }
    } catch (err) {
      console.error("Ошибка сброса пароля:", err);
    } finally {
      setIsSending(false);
    }
  };

  return (
    <div className="container">
      <div className="tabs">
        {activeTab === 'login' ? 'Вход' : 'Регистрация'}
      </div>

      {activeTab === 'login' && (
        <form className="tab-content" onSubmit={(e) => { e.preventDefault(); login(user); }}>

          <input className="input" name="login" value={user.login} type="text" placeholder="Логин" onChange={handleInputChange} required />
          <input className="input" name="password" value={user.password} type="password" placeholder="Пароль" onChange={handleInputChange} required />

          <div className="options">
            <a href="#!" onClick={() => setIsForgotModalOpen(true)}>Забыли данные для входа?</a>
          </div>

          <button className="btn" type="submit">Войти</button>
          <p className="text-center">
            Не зарегистрированы? <a href="#!" onClick={() => handleTabClick('register')}>Зарегистрироваться</a>
          </p>
        </form>
      )}

      {activeTab === 'register' && (
        <form className="tab-content" onSubmit={(e) => { e.preventDefault(); postUser(); }}>

          <input className="input" name='login' value={user.login} type="text" placeholder="Логин" onChange={handleInputChange} required />
          <input className="input" name='email' value={user.email} type="email" placeholder="Электронная почта" onChange={handleInputChange} required />
          <input className="input" name='password' value={user.password} type="password" placeholder="Пароль" onChange={handleInputChange} required />

          <button className="btn" type="submit">Зарегистрироваться</button>
          <p className="text-center">
            Уже есть аккаунт? <a href="#!" onClick={() => handleTabClick('login')}>Войти</a>
          </p>
        </form>
      )}
      {isForgotModalOpen && (
        <div className={`modal-overlay ${isClosing ? 'closing' : ''}`} onClick={closeForgotModal}>
          <div className={`recovery-modal-content ${isClosing ? 'closing' : ''}`} onClick={e => e.stopPropagation()}>

            <div className="modal-close-x" onClick={closeForgotModal}>
              <FontAwesomeIcon icon={faTimes} />
            </div>

            <div className="recovery-modal-title">Восстановление доступа</div>
            <p className="recovery-modal-text">Введите email, указанный при регистрации:</p>

            <form onSubmit={handlePasswordReset}>
              <div className="recovery-input-container">
                <label className="text">Email</label>
                <input
                  className="input"
                  type="email"
                  value={resetEmail}
                  onChange={(e) => setResetEmail(e.target.value)}
                  required
                  placeholder="example@mail.com"
                />
              </div>

              <button className="recovery-btn-send" type="submit" disabled={isSending}>
                {isSending ? "Отправка..." : "Отправить письмо"}
              </button>

              <div className="recovery-btn-cancel" onClick={closeForgotModal}>
                Отмена
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
