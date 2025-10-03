import '../styles/header.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faCameraRetro } from '@fortawesome/free-solid-svg-icons'
import { Link } from 'react-router-dom';

export function Header() {



    return (
        <header>
            <div className='logo'>
                <FontAwesomeIcon className='logo-icon' icon={faCameraRetro}/>
                <a href='/'>SharePhoto</a>
            </div>
            <nav>
                <ul>
                    <li><a href="/">О нас</a></li>
                    <li><a href="/">Инструкции</a></li>
                    <li><a href="/help">Помощь</a></li>
                </ul>
            </nav>
            <Link to={`/login`}className="login">
                <FontAwesomeIcon icon={faUser} />
            </Link>
        </header>
    );
}


