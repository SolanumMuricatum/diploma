import '../styles/header.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelopeSquare, faUser } from '@fortawesome/free-solid-svg-icons'
import { faRightFromBracket } from '@fortawesome/free-solid-svg-icons'
import { faCameraRetro } from '@fortawesome/free-solid-svg-icons'
import { faEnvelope } from '@fortawesome/free-solid-svg-icons'
import { faGear } from '@fortawesome/free-solid-svg-icons'
import { useAuth } from '../auth/AuthProvider';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from "react";


export function Header() {
    const { user, userId, logout, checkAuth } = useAuth();

    const [hasNewInvites, setHasNewInvites] = useState(false);

    useEffect(() => {
        if (!userId) return;

        const checkInvites = async () => {
            try {
                const res = await fetch(`http://localhost:8080/album/invitation/check-pending?userId=${userId}`, {
                    credentials: 'include'
                });
                if (res.ok) {
                    const data = await res.json();
                    setHasNewInvites(data);
                }
            } catch (err) {
                console.error("Ошибка проверки уведомлений", err);
            }
        };

        checkInvites();
        const interval = setInterval(checkInvites, 60000);
        return () => clearInterval(interval);
    }, [userId]);

    useEffect(() => {
        checkAuth();
    }, [userId]);

    const navigate = useNavigate();

    const handleAccountButton = () => {
        navigate('/account');
    }

    const handleExitClick = () => {
        logout();
    }

    return (
        <header>
            <div className='logo'>
                {!userId && (
                    <Link className='logo-wrapper' to={`/`}>
                        <FontAwesomeIcon className='logo-icon' icon={faCameraRetro} />
                        <div>SharePhoto</div>
                    </Link>
                )}
                {userId && (
                    <Link className='logo-wrapper' to={`/main`}>
                        <FontAwesomeIcon className='logo-icon' icon={faCameraRetro} />
                        <div>SharePhoto</div>
                    </Link>
                )}
            </div>
            <nav>
                <ul>
                    <li><a href="/">О нас</a></li>
                    <li><a href="/">Инструкции</a></li>
                    <li><a href="/help">Помощь</a></li>
                    {userId && (
                        <div className='note-icon'>
                            <Link to={`/invitations/${userId}`}>
                                <FontAwesomeIcon icon={faEnvelope} />
                            </Link>
                            {hasNewInvites && (
                                <div style={{
                                    position: 'absolute',
                                    top: '15px',
                                    right: '560px',
                                    width: '10px',
                                    height: '10px',
                                    backgroundColor: '#0C7F69',
                                    borderRadius: '50%',
                                    border: '2px solid white'
                                }} />
                            )}
                        </div>
                    )}
                </ul>
            </nav>
            {!userId && (
                <Link to={`/login`} className="login">
                    <FontAwesomeIcon icon={faUser} />
                </Link>
            )}
            {userId && (
                <div className='header-authenticated-container'>
                    {user?.photo ? (
                        <img className='header-account-button-container' src={user?.photo} onClick={handleAccountButton} />
                    ) : (
                        <div className='header-account-button-container' onClick={handleAccountButton}>
                            <FontAwesomeIcon className='header-account-photo-icon' icon={faUser} />
                        </div>
                    )}
                    <div className='exit-icon'>
                        <FontAwesomeIcon onClick={handleExitClick} icon={faRightFromBracket} />
                    </div>
                </div>
            )}
        </header>
    );
}


