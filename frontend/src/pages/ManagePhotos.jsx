import React, { useState } from 'react';
import { toast } from 'react-toastify';
import '../styles/managePhotos.css';
import { useAuth } from '../auth/AuthProvider';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'
import { Link, useParams } from 'react-router-dom';
import { Album } from '../connection/Album'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faCalendar } from '@fortawesome/free-solid-svg-icons'
import { PhotosOfUserToUpload } from '../connection/PhotosOfUserToUpload';

export const ManagePhotos = () => {
    const [creator, setCreator] = useState();
    const [startDate, setStartDate] = useState();
    const [endDate, setEndDate] = useState();
    const [slots, setSlots] = useState(Array(10).fill(null));
    const { parent } = useParams();

    return (
        <div className='main-manage-photos-container'>
            <Link to={`/albums/${parent}`}>
                <div className='manage-photos-arrow-right-container'>
                    <FontAwesomeIcon icon={faArrowLeft}/>
                </div>                
            </Link>
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
                <div className='manage-photos-control-panel-options'>                        
                    <div className='manage-photos-album-description'>Добавьте 10 лучших фото!</div>
                </div>
            </div>
            <PhotosOfUserToUpload slots={slots} setSlots={setSlots}/>
        </div>
    );
};