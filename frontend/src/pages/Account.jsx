import React, { useState } from 'react';
import { toast } from 'react-toastify';
import '../styles/account.css';
import { useAuth } from '../auth/AuthProvider';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from '@fortawesome/free-solid-svg-icons'

export const Account = () => {
  const { user, userId, logout, checkAuth } = useAuth();
/*   const [activeTab, setActiveTab] = useState('login');
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

  async function apiFetch(url, options = {}) {
    try {
      const response = await fetch(url, options);
      const data = await response.json().catch(() => ({}));

      if (!response.ok) {
        throw new Error(data.message || `Ошибка ${response.status}`);
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
  }; */

  return (
    <div className='account-container' >
        <div className="account-data-container">
          <div className='account-photo-container'>
            <img className='account-photo' src={user?.photo} />
            <FontAwesomeIcon className='photo-edit-icon' icon={faPencil}/>
          </div>
            <div className='account-login'>
              <label for="input" class="text">Login:</label>
              <input type="text" placeholder="Пишите здесь..." name="input" value={'pepino'} class="input" maxLength={50}/>
            </div>
            <div className='account-email'>
              <label for="input" class="text">Email:</label>
              <input type="text" placeholder="Пишите здесь..." name="input" value={'pepino@gmail.com'} class="input" maxLength={50}/>
            </div>
            <div className='account-password'>
              <button>Change password</button>
            </div>
            <div className='account-delete'>
              <button>Delete account</button>
            </div>
        </div>
    </div>
  );
};
