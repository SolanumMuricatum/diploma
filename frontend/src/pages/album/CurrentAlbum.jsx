import '../../styles/currentAlbum.css';
import { Album } from '../../connection/Album'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faCalendar } from '@fortawesome/free-solid-svg-icons'
import { faFilter } from '@fortawesome/free-solid-svg-icons'
import { ReactComponent as CameraIcon } from '../../photo/photo-camera-svgrepo-com.svg';
import { ReactComponent as PhotoIcon } from '../../photo/picture-svgrepo-com.svg';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'; // Убедитесь, что это правильно

export function CurrentAlbum() {
    const [creator, setCreator] = useState();
    const [startDate, setStartDate] = useState();
    const [endDate, setEndDate] = useState();
    return (
        <div>
            <Album setCreator={setCreator} setStartDate={setStartDate} setEndDate={setEndDate}/>
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
                    <button>Выделить фото</button>
                    <button>Выделить всё</button>
                    <button>Скачать</button>
                    <FontAwesomeIcon icon={faFilter} />
                </div>
            </div>
            <div>
                <div className='friend-data-container-wrapper'>
                    <div className='friend-data-container'>
                        <div className='friend-avatar'></div>
                        <div className='friend-name'>pepino</div>
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


