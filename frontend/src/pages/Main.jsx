import '../styles/main.css';
import { Event } from '../connection/Event'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { ReactComponent as CameraIcon } from '../photo/photo-camera-svgrepo-com.svg';
import { ReactComponent as PhotoIcon } from '../photo/picture-svgrepo-com.svg';
import { faArrowUp, faDirections } from '@fortawesome/free-solid-svg-icons'
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'; // Убедитесь, что это правильно

export function Main() {
    return (
        <div>
            <Event/>
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


