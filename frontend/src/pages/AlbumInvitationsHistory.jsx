import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import '../styles/manageMembers.css';
import { useAuth } from '../auth/AuthProvider';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'
import { Link, useParams } from 'react-router-dom';
import { Album } from '../connection/Album'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faCalendar } from '@fortawesome/free-solid-svg-icons'
import { PhotosOfUserToUpload } from '../connection/PhotosOfUserToUpload';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

export const AlbumInvitationsHistory = () => {
    const [creator, setCreator] = useState();
    const [startDate, setStartDate] = useState();
    const { albumId } = useParams();
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

        const fetchData = async () => {
            try {
                const invRes = await fetch(`http://localhost:8080/album/get/all/album/invitations?albumId=${albumId}`, {
                    credentials: 'include'
                });
                if (!invRes.ok) return;
                const invitationsData = await invRes.json();

                if (invitationsData.length === 0) {
                    setMembers([]);
                    return;
                }

                const ids = invitationsData.map(inv => inv.id.userId).join(',');

                const userRes = await fetch(`http://localhost:8080/user/getAll?ids=${ids}`, {
                    credentials: 'include'
                });
                if (!userRes.ok) return;
                const usersData = await userRes.json();

                const mergedData = invitationsData.map(invite => {
                    const userDetails = usersData.find(u => u.id === invite.id.userId);
                    return {
                        userId: invite.id.userId,
                        login: userDetails?.login || 'Загрузка...',
                        photo: userDetails?.photo || 'default-avatar.png',
                        status: invite.accepted // null, true или false
                    };
                });

                setMembers(mergedData);
            } catch (err) {
                console.error("Ошибка при сборке данных участников:", err);
            }
        };

        fetchData();
    }, []);

    const handleRetryInvitation = async (targetUser) => {
        const isConfirmed = window.confirm(`Вы уверены, что хотите повторно пригласить ${targetUser.login}?`);

        if (!isConfirmed) {
            return;
        }

        try {
            const res = await fetch(`http://localhost:8080/album/invitation/retry`, {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ albumId: albumId, userId: targetUser.userId, creatorLoginSnapshot: userLogin, albumName: albumName })
            });
            if (res.ok) {
                alert("Приглашение отправлено повторно!");
                window.location.reload();
            }
        } catch (err) {
            console.error("Ошибка при инвайте:", err);
        }
    };

    return (
        <div className='main-manage-members-container'>
            <Link to={`/albums/${parent}/manage/members/${albumId}`}>
                <div className='manage-members-arrow-right-container'>
                    <FontAwesomeIcon icon={faArrowLeft} />
                </div>
            </Link>
            <Album setAlbumName={setAlbumName} setCreator={setCreator} setStartDate={setStartDate} />
            <div className='album-control-panel'>
                <div className='album-creator-container'>
                    <FontAwesomeIcon icon={faUser} />
                    <div>{creator}</div>
                    <div className='album-expiration-date-container'>
                        <FontAwesomeIcon icon={faCalendar} />
                        <div>{startDate}</div>
                    </div>
                </div>
            </div>
            {members && members.length === 0 && (
                <div style={{ margin: '20px' }}>У вас нету заявок</div>
            )}
            {members.map(member => (
                <div className='friend-data-container-wrapper'>
                    <div className='friend-data-container'>
                        <img className='friend-avatar' src={member.photo} />
                        <div className='friend-name'>{member.login}</div>
                    </div>

                    <div className="status-container">
                        {member.status === true && <span>Принято</span>}

                        {member.status === null && <span>Ожидание...</span>}

                        {member.status === false && (
                            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                                <span style={{ color: 'red' }}>Отклонено</span>
                                <button
                                    className="album-invitations-button"
                                    onClick={() => handleRetryInvitation(member)}
                                >
                                    Повторить
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            ))}
        </div>
    );
};