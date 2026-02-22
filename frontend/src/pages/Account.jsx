import React, { useState, useRef, useEffect } from 'react';
import { toast } from 'react-toastify';
import '../styles/account.css';
import { useAuth } from '../auth/AuthProvider';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEllipsisVertical } from '@fortawesome/free-solid-svg-icons'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom'; 

export const Account = () => {
  const navigate = useNavigate();
  const { userId, setUserId, user, logout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuRef = useRef(null);
  const fileInputRef = useRef(null); // Реф для скрытого инпута
  const [login, setLogin] = useState(user?.username || '');
  const [email, setEmail] = useState(user?.email || '');

  // Следим за изменением объекта user и обновляем локальный стейт
  useEffect(() => {
    if (user) {
      setLogin(user.username || '');
      setEmail(user.email || '');
    }
  }, [user]); // Как только user прилетит из useAuth, стейты обновятся

  // Сравнение теперь будет работать корректно
  const isLoginChanged = login !== (user?.username || '');
  const isEmailChanged = email !== (user?.email || '');

  // Функция отправки (можно сделать одну на всё или разные)
  const handleUpdateUserLogin = async (field, value) => {
    if (!value || value.trim() === "") {
      alert("Логин не может быть пустым!");
      return; // Прерываем выполнение функции
    }

    try {
      const response = await fetch(`http://localhost:8080/user/account/login/update`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ userId, [field]: value }),
      });
      if (response.ok) {
        alert('Данные обновлены! Пожалуйста, авторизуйтесь повторно.');
        navigate(`/login`)
        window.location.reload();
      }
    } catch (e) {
      alert(e);
      console.error(e);
    }
  };

  const handleUpdateUserEmail = async (field, value) => {
    const trimmedEmail = value.trim();

    if (!trimmedEmail) {
      alert("Email не может быть пустым!");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(trimmedEmail)) {
      alert("Пожалуйста, введите корректный адрес электронной почты (например, user@mail.com)");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/user/account/email/update`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ userId, [field]: value }),
      });
      if (response.ok) {
        alert('Данные обновлены!');
        window.location.reload();
      }
    } catch (e) {
      alert(e);
      console.error(e);
    }
  };
  
  // 1. Логика выбора файла
  const handleAddPhotoClick = () => {
    fileInputRef.current.click(); // Открываем окно выбора файла
    setIsMenuOpen(false);
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        const base64String = reader.result; // Это строка base64
        postPhoto(base64String);
      };
      reader.readAsDataURL(file);
    }
  };

  // 2. POST запрос (Добавление)
  const postPhoto = async (base64String) => {
    try {
      const response = await fetch(`http://localhost:8080/user/account/save/photo`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ image: base64String, userId: userId }),
      });

      if (response.ok) {
        alert('Фото успешно обновлено!');
        window.location.reload();
      }
    } catch (error) {
      console.error('Ошибка при сохранении:', error);
    }
  };

  // 3. DELETE запрос (Удаление)
  const handleDeletePhoto = async () => {
    if (!window.confirm("Удалить фото профиля?")) return;
    
    try {
      const response = await fetch(`http://localhost:8080/user/account/delete/photo?userId=${userId}`, {
        method: 'DELETE',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' }
      });

      if (response.ok) {
        alert('Фото удалено');
        window.location.reload();
      }
    } catch (error) {
      console.error('Ошибка при удалении:', error);
    }
    setIsMenuOpen(false);
  };

  return (
    <div className='account-container'>
      {/* Скрытый инпут для выбора файла */}
      <input 
        type="file" 
        ref={fileInputRef} 
        style={{ display: 'none' }} 
        accept="image/*"
        onChange={handleFileChange}
      />

      <div className="account-data-container">
        <div className='account-photo-container' style={{ position: 'relative' }}>
          {user?.photo ? (
            <img className='account-photo' src={user.photo} alt="Profile" />
          ) : (
            <div className='account-photo'>
               <FontAwesomeIcon icon={faUser} />
            </div>
          )}
          <div 
            className='account-photo-options-icon' 
            onClick={(e) => { e.stopPropagation(); setIsMenuOpen(!isMenuOpen); }}
          >
            <FontAwesomeIcon icon={faEllipsisVertical} />
          </div>

          {isMenuOpen && (
            <div className='account-photo-options' ref={menuRef} onClick={(e) => e.stopPropagation()}>
              <div className='menu-item' onClick={handleAddPhotoClick}>Добавить фото</div>
              <div className='menu-item' onClick={handleDeletePhoto}>Удалить фото</div>
            </div>
          )}
        </div>
        <div className='account-login'>
          <label htmlFor="login" className="text">Login:</label>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <input 
              type="text" 
              id="login" 
              value={login} 
              onChange={(e) => setLogin(e.target.value)} 
              className="input" 
            />
            {isLoginChanged && (
              <button 
                className="account-save-btn" 
                onClick={() => handleUpdateUserLogin('login', login)}
              >
                Сохранить
              </button>
            )}
          </div>
        </div>

        <div className='account-email'>
          <label htmlFor="email" className="text">Email:</label>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <input 
              type="text" 
              id="email" 
              value={email} 
              onChange={(e) => setEmail(e.target.value)} 
              className="input" 
            />
            {isEmailChanged && (
              <button
                className="account-save-btn" 
                onClick={() => handleUpdateUserEmail('email', email)}
              >
                Сохранить
              </button>
            )}
          </div>
        </div>
        <div className='account-password'><button>Изменить пароль</button></div>
        <div className='account-delete'><button>Удалить аккаунт</button></div>
      </div>
    </div>
  );
};
