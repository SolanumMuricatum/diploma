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
import { useNavigate } from 'react-router-dom'; 
import { useEffect } from "react";


export function Header() {
    const { userId, logout, checkAuth } = useAuth();

    useEffect(() => {
        checkAuth();
    }, [userId]);

    const navigate = useNavigate();

    const handleAccountButton = () =>{
        navigate('/account');
    }

    const handleExitClick = () =>{
        logout();
    }

    return (
        <header>
            <div className='logo'>
                {!userId && (
                    <Link className='logo-wrapper' to={`/`}>
                        <FontAwesomeIcon className='logo-icon' icon={faCameraRetro}/>
                        <div>SharePhoto</div>
                    </Link>
                )}
                {userId && (
                    <Link className='logo-wrapper' to={`/main`}>
                        <FontAwesomeIcon className='logo-icon' icon={faCameraRetro}/>
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
                            <FontAwesomeIcon icon={faEnvelope} />
                        </div>
                    )}
                </ul>
            </nav>
            {!userId && (
                <Link to={`/login`}className="login">
                    <FontAwesomeIcon icon={faUser} />
                </Link>
            )}
            {userId && (
                <div className='header-authenticated-container'>
                    <div className='header-account-button-container' onClick={handleAccountButton}></div>
                    <div className='gear-icon'>
                        <FontAwesomeIcon onClick={handleExitClick}  icon={faGear} />
                    </div>
                    <div className='exit-icon'>
                        <FontAwesomeIcon onClick={handleExitClick}  icon={faRightFromBracket} />
                    </div>
                </div>
            )}
        </header>
    );
}


