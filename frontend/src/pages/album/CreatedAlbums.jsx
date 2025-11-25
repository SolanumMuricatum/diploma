import '../../styles/createdAlbums.css';
import { Event } from '../../connection/Album'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { ReactComponent as CameraIcon } from '../../photo/photo-camera-svgrepo-com.svg';
import { ReactComponent as PhotoIcon } from '../../photo/picture-svgrepo-com.svg';
import { faArrowUp, faDirections } from '@fortawesome/free-solid-svg-icons'
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'; // Убедитесь, что это правильно
import { faEllipsisVertical } from '@fortawesome/free-solid-svg-icons'
import { faCalendar } from '@fortawesome/free-solid-svg-icons'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faPlus } from '@fortawesome/free-solid-svg-icons'

export function CreatedAlbums() {
    return (
        <div className='created-albums-page-container'>
            <div className='created-albums-container'>
                <div style={{marginBottom: '100px', fontSize: '18pt'}}>--- Созданные фотоальбомы ---</div>
                <div className='created-albums-wrapper'>
                    <div className='created-album-card'>
                        <div className='created-album-image-container'>
                            <div className='created-album-image'></div>
                        </div>
                        <div>
                            <div className='created-album-title'>Море обнимет закопает пески, закинут рыболовы лески</div>
                            <div className='created-album-creator'>
                                <FontAwesomeIcon icon={faUser} />
                                pepino
                            </div>
                            <div className='created-album-date'>
                                <FontAwesomeIcon icon={faCalendar} />
                                11.03.2026 - 11.04.2026
                            </div>
                            <div className='created-album-options'>
                                <FontAwesomeIcon icon={faEllipsisVertical} />
                            </div>
                        </div>
                    </div>   
                    <div className='created-album-card'>
                        <div className='created-album-image-container'>
                            <div className='created-album-image'></div>
                        </div>
                        <div>
                            <div className='created-album-title'>Море обнимет закопает пески, закинут рыболовы лески</div>
                            <div className='created-album-creator'>
                                <FontAwesomeIcon icon={faUser} />
                                pepino
                            </div>
                            <div className='created-album-date'>
                                <FontAwesomeIcon icon={faCalendar} />
                                11.03.2026 - 11.04.2026
                            </div>
                            <div className='created-album-options'>
                                <FontAwesomeIcon icon={faEllipsisVertical} />
                            </div>
                        </div>
                    </div>   
                    <div className='created-album-card'>
                        <div className='created-album-image-container'>
                            <div className='created-album-image'></div>
                        </div>
                        <div>
                            <div className='created-album-title'>Море обнимет закопает пески, закинут рыболовы лески</div>
                            <div className='created-album-creator'>
                                <FontAwesomeIcon icon={faUser} />
                                pepino
                            </div>
                            <div className='created-album-date'>
                                <FontAwesomeIcon icon={faCalendar} />
                                11.03.2026 - 11.04.2026
                            </div>
                            <div className='created-album-options'>
                                <FontAwesomeIcon icon={faEllipsisVertical} />
                            </div>
                        </div>
                    </div>   
                    <div className='created-album-card'>
                        <div className='created-album-image-container'>
                            <div className='created-album-image'></div>
                        </div>
                        <div>
                            <div className='created-album-title'>Море обнимет закопает пески, закинут рыболовы лески</div>
                            <div className='created-album-creator'>
                                <FontAwesomeIcon icon={faUser} />
                                pepino
                            </div>
                            <div className='created-album-date'>
                                <FontAwesomeIcon icon={faCalendar} />
                                11.03.2026 - 11.04.2026
                            </div>
                            <div className='created-album-options'>
                                <FontAwesomeIcon icon={faEllipsisVertical} />
                            </div>
                        </div>
                    </div>   
                    <div className='created-album-card'>
                        <div className='created-album-image-container'>
                            <div className='created-album-image'></div>
                        </div>
                        <div>
                            <div className='created-album-title'>Море обнимет закопает пески, закинут рыболовы лески</div>
                            <div className='created-album-creator'>
                                <FontAwesomeIcon icon={faUser} />
                                pepino
                            </div>
                            <div className='created-album-date'>
                                <FontAwesomeIcon icon={faCalendar} />
                                11.03.2026 - 11.04.2026
                            </div>
                            <div className='created-album-options'>
                                <FontAwesomeIcon icon={faEllipsisVertical} />
                            </div>
                        </div>
                    </div>    
                    <div className='created-album-card'>
                        <div className='created-album-image-container'>
                            <div className='created-album-image'></div>
                        </div>
                        <div>
                            <div className='created-album-title'>Море обнимет закопает пески, закинут рыболовы лески</div>
                            <div className='created-album-creator'>
                                <FontAwesomeIcon icon={faUser} />
                                pepino
                            </div>
                            <div className='created-album-date'>
                                <FontAwesomeIcon icon={faCalendar} />
                                11.03.2026 - 11.04.2026
                            </div>
                            <div className='created-album-options'>
                                <FontAwesomeIcon icon={faEllipsisVertical} />
                            </div>
                        </div>
                    </div>    
                </div>      
            </div>
            <div className='albums-created-add-icon'>
                <FontAwesomeIcon icon={faPlus} />
            </div> 
        </div>
    );
}


