import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import '../styles/userInvitationsHistory.css';
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

export const UserInvitationsHistory = () => {
    const [invitations, setInvitations] = useState([]);
    const { userId, userLogin, setUserId, setUserLogin } = useAuth();
    useEffect(() => {
        const fetchData = async () => {
            try {
                const invRes = await fetch(`http://localhost:8080/album/get/all/user/invitations?userId=${userId}`, {
                    credentials: 'include'
                });
                if (!invRes.ok) return;
                const invitationsData = await invRes.json();

                if (invitationsData.length === 0) {
                    setInvitations([]);
                    return;
                }
                setInvitations(invitationsData);
            } catch (err) {
                console.error("Ошибка при сборке данных приглашений:", err);
            }
        };

        fetchData();
    }, []);

    const handleAcceptInvitation = async (invitation) => {
        const isConfirmed = window.confirm(`Вы уверены, что хотите принять приглашение от ${invitation.creatorLoginSnapshot}?`);

        if (!isConfirmed) {
            return;
        }

        try {
            const res = await fetch(`http://localhost:8080/album/invitation/accept?albumId=${invitation.id.albumId}&userId=${userId}`, {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
            });
            if (res.ok) {
                alert("Приглашение принято! Поспешите добавить фото!");
                window.location.reload();
            }
        } catch (err) {
            console.error("Ошибка при отправке данных:", err);
        }
    };

    const handleDeclineInvitation = async (invitation) => {
        const isConfirmed = window.confirm(`Вы уверены, что хотите отклонить приглашение от ${invitation.creatorLoginSnapshot}?`);

        if (!isConfirmed) {
            return;
        }

        try {
            const res = await fetch(`http://localhost:8080/album/invitation/decline?albumId=${invitation.id.albumId}&userId=${userId}`, {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
            });
            if (res.ok) {
                alert("Приглашение отклонено!");
                window.location.reload();
            }
        } catch (err) {
            console.error("Ошибка при отправке данных:", err);
        }
    };

    return (
        <div className='main-manage-members-container'>
            <div style={{ margin: "20px", fontSize: "16pt", textAlign: "center" }}>----- Заявки -----</div>
            {invitations && invitations.length === 0 && (
                <div style={{ margin: '20px' }}>У вас нету приглашений</div>
            )}
            {invitations.map(inv => (
                <div className='friend-data-container-wrapper' style={{ borderTop: "2px solid black" }}>
                    <div className='friend-data-container'>
                        <div className='friend-name'><b>{inv.creatorLoginSnapshot}</b>{"\u00A0"}приглашает Вас в фотоальбом{"\u00A0"}"<b>{inv.albumName}</b>"</div>
                    </div>

                    <div className="status-container">
                        {inv.accepted === true && <span>Принято</span>}

                        {inv.accepted === false && <span>Отклонено</span>}

                        {inv.accepted === null && (
                            <div style={{ display: 'flex', gap: '10px', marginLeft: 'auto' }}>
                                <button
                                    onClick={() => handleAcceptInvitation(inv)}
                                    className="invitations-button"
                                >
                                    Принять
                                </button>

                                <button
                                    onClick={() => handleDeclineInvitation(inv)}
                                    className="invitations-button"
                                >
                                    Отклонить
                                </button>
                            </div>
                        )}

                    </div>
                </div>
            ))}
        </div>
    );
};