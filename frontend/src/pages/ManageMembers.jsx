import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import '../styles/manageMembers.css';
import { useAuth } from '../auth/AuthProvider';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'
import { Link, useParams, useNavigate } from 'react-router-dom';
import { Album } from '../connection/Album'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faCalendar } from '@fortawesome/free-solid-svg-icons'
import { PhotosOfUserToUpload } from '../connection/PhotosOfUserToUpload';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

export const ManageMembers = () => {
    const navigate = useNavigate();
    const [creator, setCreator] = useState();
    const [startDate, setStartDate] = useState();
    const { albumId } = useParams();
    const [endDate, setEndDate] = useState();
    const [members, setMembers] = useState([]);
    const [albumName, setAlbumName] = useState([]);
    const { parent } = useParams();
    const { userId, userLogin, setUserId, setUserLogin } = useAuth();
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState("");
    const [searchResults, setSearchResults] = useState([]);
    const [isSearching, setIsSearching] = useState(false);


    useEffect(() => {
        if (!albumId) return;

        const fetchAllData = async () => {
            try {
                const res = await fetch(`http://localhost:8080/album/get/members?albumId=${albumId}`, {
                    credentials: 'include'
                });
                if (res.ok) {
                    const membersData = await res.json();
                    setMembers(membersData);
                }
            } catch (err) {
                console.error("Ошибка загрузки участников:", err);
            }
        };

        fetchAllData();
    }, [albumId]);

    const handleDeleteAlbumMember = async (userId) => {
        const isConfirmed = window.confirm("Вы уверены, что хотите удалить этого участника?");

        if (!isConfirmed) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/album/added/leave`, {
                method: 'DELETE',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },

                body: JSON.stringify({
                    albumId: albumId,
                    userId: userId
                }),
            });

            if (response.status === 409) {
                setUserId(null);
                setUserLogin(null);
                return false;
            }

            if (response.ok) {
                alert('Вы успешно удалили участника! Все его фото были удалены.');
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
    };

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            if (searchQuery.length >= 2) { // Начинаем искать от 2-х символов
                fetchUsers(searchQuery);
            } else {
                setSearchResults([]);
            }
        }, 500); // Ждем 500мс после последнего нажатия клавиши

        return () => clearTimeout(delayDebounceFn);
    }, [searchQuery]);

    const fetchUsers = async (query) => {
        setIsSearching(true);
        try {
            const res = await fetch(`http://localhost:8080/user/search?query=${query}&albumId=${albumId}`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },

            });
            if (res.ok) {
                const data = await res.json();
                setSearchResults(data);
            }
        } catch (err) {
            console.error("Ошибка поиска:", err);
        } finally {
            setIsSearching(false);
        }
    };

    const handleInvite = async (targetUser) => {
        const isConfirmed = window.confirm(`Вы уверены, что пригласить ${targetUser.login}?`);

        if (!isConfirmed) {
            return;
        }

        try {
            const res = await fetch(`http://localhost:8080/album/save/invitation`, {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ albumId: albumId, userId: targetUser.id, creatorLoginSnapshot: userLogin, albumName: albumName })
            });
            if (res.ok) {
                alert("Приглашение отправлено!");
                setSearchResults(prev => prev.filter(u => u.id !== targetUser.id));
            }
        } catch (err) {
            console.error("Ошибка при инвайте:", err);
        }
    };

    return (
        <div className='main-manage-members-container'>
            <Link to={`/albums/${parent}`}>
                <div className='manage-members-arrow-right-container'>
                    <FontAwesomeIcon icon={faArrowLeft} />
                </div>
            </Link>
            <Album setAlbumName={setAlbumName} setCreator={setCreator} setStartDate={setStartDate} setEndDate={setEndDate} />
            <div className='album-control-panel'>
                <div className='album-creator-container'>
                    <FontAwesomeIcon icon={faUser} />
                    <div>{creator}</div>
                    <div className='album-expiration-date-container'>
                        <FontAwesomeIcon icon={faCalendar} />
                        <div>{startDate} - {endDate}</div>
                    </div>
                </div>
                <div className='album-control-panel-options'>
                    <button onClick={() => setIsModalOpen(true)}>
                        Добавить
                    </button>
                    <button onClick={() => navigate(`/albums/${parent}/members/history/${albumId}`)}>
                        История заявок
                    </button>
                </div>
            </div>
            {members && members.length === 0 && (
                <div style={{ margin: '20px' }}>У вас нету участников фотоальбома</div>
            )}
            {members && members.length > 0 && members.map(member => (
                <div key={member.id} className="member-block">
                    <div className='friend-data-container-wrapper'>
                        <div className='friend-data-container'>
                            <img className='friend-avatar' src={member.photo} alt={member.login} />
                            <div className='friend-name'>{member.login}</div>
                        </div>
                        <div
                            onClick={() => handleDeleteAlbumMember(member.id)}
                            style={{
                                width: '30px',
                                height: '30px',
                                backgroundColor: 'white',
                                borderRadius: '50%',
                                border: '1px solid black',
                                cursor: 'pointer',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center'
                            }}
                        >
                            <FontAwesomeIcon icon={faTrash} />
                        </div>
                    </div>
                </div>
            ))}

            {isModalOpen && (
                <div className="modal-overlay" onClick={() => setIsModalOpen(false)}>
                    <div className="search-modal-content" onClick={e => e.stopPropagation()}>
                        <div className="modal-close-icon" onClick={() => setIsModalOpen(false)}>
                            <FontAwesomeIcon icon={faTimes} />
                        </div>

                        <h3 style={{ color: '#112250', marginBottom: '20px' }}>Поиск пользователей</h3>

                        <div className="search-input-wrapper">
                            <label className="text">Логин</label>
                            <input
                                type="text"
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                placeholder="Начните вводить..."
                            />
                        </div>

                        <div className="search-results-list">
                            {isSearching && <div style={{ textAlign: 'center' }}>Ищем...</div>}

                            {searchResults.map(user => (
                                <div key={user.id} className="search-item">
                                    <div className="search-user-info">
                                        <img src={user.photo} alt="" />
                                        <span className="search-user-name">{user.login}</span>
                                    </div>
                                    <button className="invite-btn" onClick={() => handleInvite(user)}>
                                        Пригласить
                                    </button>
                                </div>
                            ))}

                            {!isSearching && searchQuery.length > 1 && searchResults.length === 0 && (
                                <div style={{ textAlign: 'center', opacity: 0.6 }}>Никого не нашли :(</div>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};