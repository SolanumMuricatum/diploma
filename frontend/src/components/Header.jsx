import '../styles/header.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faCameraRetro } from '@fortawesome/free-solid-svg-icons'

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
                    <li><a href="/about">Помощь</a></li>
                </ul>
            </nav>
            <a className='login' href='/'><FontAwesomeIcon icon={faUser}/></a> 
        </header>
    );
}


