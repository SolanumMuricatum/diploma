import '../styles/main.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { ReactComponent as CameraIcon } from '../photo/photo-camera-svgrepo-com.svg';
import { ReactComponent as PhotoIcon } from '../photo/picture-svgrepo-com.svg';
import { faArrowUp, faDirections } from '@fortawesome/free-solid-svg-icons'

export function Main() {
    return (
        <div>
            <div className='event-title'>
                Гендерпати у Миши и Светы ♡
            </div>
            <div style={{position: 'sticky', top: '0px', borderBottom: '2px solid black', backgroundColor: 'aquamarine'}}>фильтры</div>
            <div>
                <div className='friend-data-container-wrapper'>
                    <div className='friend-data-container'>
                        <div className='friend-avatar'></div>
                        <div className='friend-name'>Анна Пепинова</div>
                    </div>
                    <div className='friend-select-all-container'>
                        <label for="select-all">Выделить всё</label>
                        <input type="checkbox" id="select-all" name="drone" value="//"/>
                    </div>
                </div>
                <div className='friend-photo-container'>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                </div>
            </div>
            <div>
                <div className='friend-data-container-wrapper'>
                    <div className='friend-data-container'>
                        <div className='friend-avatar'></div>
                        <div className='friend-name'>Анна Пепинова</div>
                    </div>
                    <div className='friend-select-all-container'>
                        <label for="select-all">Выделить всё</label>
                        <input type="checkbox" id="select-all" name="drone" value="//"/>
                    </div>
                </div>
                <div className='friend-photo-container'>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                </div>
            </div>            
            <div>
                <div className='friend-data-container-wrapper'>
                    <div className='friend-data-container'>
                        <div className='friend-avatar'></div>
                        <div className='friend-name'>Анна Пепинова</div>
                    </div>
                    <div className='friend-select-all-container'>
                        <label for="select-all">Выделить всё</label>
                        <input type="checkbox" id="select-all" name="drone" value="//"/>
                    </div>
                </div>
                <div className='friend-photo-container'>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                    <div className='friend-photo'></div>
                </div>
            </div>
        </div>

    );
}


