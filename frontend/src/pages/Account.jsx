import React, { useState, useRef, useEffect } from 'react';
import { toast } from 'react-toastify';
import '../styles/account.css';
import { useAuth } from '../auth/AuthProvider';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEllipsisVertical } from '@fortawesome/free-solid-svg-icons'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom';
import { faXmark } from '@fortawesome/free-solid-svg-icons';

export const Account = () => {
	const navigate = useNavigate();
	const { userId, setUserId, user, setUserLogin, logout } = useAuth();
	const [isMenuOpen, setIsMenuOpen] = useState(false);
	const menuRef = useRef(null);
	const fileInputRef = useRef(null); // Реф для скрытого инпута
	const [login, setLogin] = useState(user?.username || '');
	const [email, setEmail] = useState(user?.email || '');
	const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);
	const [passwordData, setPasswordData] = useState({ oldPassword: '', newPassword: '' });
	const [isClosing, setIsClosing] = useState(false);

	const handlePasswordChange = (e) => {
		const { name, value } = e.target;
		setPasswordData(prev => ({ ...prev, [name]: value }));
	};

	const closeModal = () => {
		setIsClosing(true); // Запускаем анимацию исчезновения
		setTimeout(() => {
			setIsPasswordModalOpen(false); // Реально удаляем из DOM
			setIsClosing(false); // Сбрасываем флаг для следующего раза
			setPasswordData({ oldPassword: '', newPassword: '' }); // Чистим поля
		}, 300); // Время должно совпадать с длительностью анимации в CSS
	};

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
				method: 'PUT',
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
				method: 'PUT',
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
			alert('Произошла ошибка при сохрании фото');
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
			alert('Ошибка удаления');
		}
		setIsMenuOpen(false);
	};

	const submitPasswordChange = async () => {
		if (!passwordData.oldPassword || !passwordData.newPassword) {
			alert("Заполните оба поля");
			return;
		}

		try {
			const response = await fetch(`http://localhost:8080/user/account/password/update`, {
				method: 'PUT',
				headers: { 'Content-Type': 'application/json' },
				credentials: 'include',
				body: JSON.stringify({
					userId,
					oldPassword: passwordData.oldPassword,
					newPassword: passwordData.newPassword
				}),
			});

			if (response.ok) {
				alert("Пароль успешно изменен!");
				setIsPasswordModalOpen(false);
				setPasswordData({ oldPassword: '', newPassword: '' });
			} else {
				const errorData = await response.json();
				alert(errorData.error || "Ошибка при смене пароля");
			}

		} catch (e) {
			alert(e);
			console.error(e);
		}
	};

	const handleDeleteAccount = async () => {
		const isConfirmed = window.confirm("Вы уверены, что хотите удалить аккаунт?");

		if (!isConfirmed) return;

		const password = window.prompt("Для подтверждения введите ваш пароль:");

		if (password !== null) {
			if (password.trim() === "") {
				try {
					const response = await fetch(`http://localhost:8080/user/delete`, {
						method: 'DELETE',
						credentials: 'include',
						headers: {
							'Content-Type': 'application/json',
						},

						body: JSON.stringify({
							password: password,
							userId: userId
						}),
					});

					if (response.status === 409) {
						setUserId(null);
						setUserLogin(null);
						return false;
					}

					if (response.ok) {
						alert('Ваш аккаунт успешно удалён!');
						window.location.reload();
						return true;
					} else {
						console.error('Что-то пошло не так...');
					}
					return response.ok;
				} catch (error) {
					console.error('Ошибка при удалении:', error);
					return false;
				}
			} else {
				console.log("Отправляем на сервер пароль:", password);
			}
		}
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
					<label htmlFor="login" className="text">Логин:</label>
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
					<label htmlFor="email" className="text">Электронная почта:</label>
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
				<div className='account-password'>
					<button onClick={() => setIsPasswordModalOpen(true)}>Изменить пароль</button>
				</div>
				{isPasswordModalOpen && (
					<div className={`modal-overlay ${isClosing ? 'closing' : ''}`} onClick={closeModal}>
						<div className={`modal-content ${isClosing ? 'closing' : ''}`} onClick={(e) => e.stopPropagation()}>

							<div className="modal-close-icon" onClick={closeModal}>
								<FontAwesomeIcon icon={faXmark} />
							</div>

							<h3 style={{ textAlign: 'center', color: '#112250', marginBottom: '10px' }}>Изменение пароля</h3>

							<div className="modal-body">
								{/* Используем твои стили .account-login */}
								<div className='account-login' style={{ width: '100%' }}>
									<label className="text">Старый пароль:</label>
									<input
										type="password"
										name="oldPassword"
										value={passwordData.oldPassword}
										onChange={handlePasswordChange}
										className="input"
										placeholder="Введите старый пароль"
										style={{ width: '100%' }} // Растягиваем на ширину модалки
									/>
								</div>

								{/* Используем твои стили .account-email (они идентичны логину) */}
								<div className='account-email' style={{ width: '100%' }}>
									<label className="text">Новый пароль:</label>
									<input
										type="password"
										name="newPassword"
										value={passwordData.newPassword}
										onChange={handlePasswordChange}
										className="input"
										placeholder="Введите новый пароль"
										style={{ width: '100%' }}
									/>
								</div>
							</div>

							<button
								className="change-password-btn"
								onClick={submitPasswordChange}
								style={{ width: '100%', height: '50px', marginTop: '30px' }}
							>
								Сохранить
							</button>
						</div>
					</div>
				)}
				<div className='account-delete'><button onClick={handleDeleteAccount}>Удалить аккаунт</button></div>
			</div>
		</div>
	);
};
