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
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'
import { Link } from 'react-router-dom';

export function CurrentAlbum() {
    const { albumId } = useParams();
    const { parent } = useParams();
    const [members, setMembers] = useState([]);
    const [photosByUser, setPhotosByUser] = useState({}); // { userId: [photo1, photo2, ...] }
    const [selectedPhotos, setSelectedPhotos] = useState(new Set()); // Храним ID выбранных фото
    const [creator, setCreator] = useState();
    const [startDate, setStartDate] = useState();
    const [endDate, setEndDate] = useState();

    useEffect(() => {
        if (!albumId) return;
        
        const fetchAllData = async () => {
            try {
                const res = await fetch(`http://localhost:8080/album/get/all/members?albumId=${albumId}`, {
                    credentials: 'include'
                });
                if (res.ok) {
                    const membersData = await res.json();
                    setMembers(membersData);
                    
                    // 2. Сразу загружаем фото для каждого участника
                    membersData.forEach(member => {
                        fetchUserPhotos(member.id);
                        console.log(member.login);
                    });

                    console.log(membersData);
                }
            } catch (err) {
                console.error("Ошибка загрузки участников:", err);
            }
        };

        fetchAllData();
    }, [albumId]);

    const fetchUserPhotos = async (userId) => {
        try {
            const res = await fetch(`http://localhost:8080/album/get/all/photo?albumId=${albumId}&userId=${userId}`, {
                credentials: 'include'
            });
            if (res.ok) {
                const data = await res.json();
                // Заполняем до 10 слотов для красоты верстки
                const slots = Array(10).fill(null).map((_, i) => data[i] || null);
                
                setPhotosByUser(prev => ({
                    ...prev,
                    [userId]: slots
                }));
            }
        } catch (err) {
            console.error(`Ошибка загрузки фото для ${userId}:`, err);
        }
    };

    // 3. Обработка выделения
    const togglePhotoSelection = (photoId) => {
        if (!photoId) return;
        const newSelection = new Set(selectedPhotos);
        if (newSelection.has(photoId)) {
            newSelection.delete(photoId);
        } else {
            newSelection.add(photoId);
        }
        setSelectedPhotos(newSelection);
    };

    return (
        <div style={{ position: 'relative' }}>
            <Link to={`/albums/${parent}`}>
                <div className='album-arrow-right-container'><FontAwesomeIcon icon={faArrowLeft} /></div>
            </Link>
            
            <Album setCreator={setCreator} setStartDate={setStartDate} setEndDate={setEndDate} />

            {/* Панель управления (теперь кнопки могут что-то делать) */}
            <div className='album-control-panel'>
                <div className='album-creator-container'>
                    <FontAwesomeIcon icon={faUser} /> <div>{creator}</div>
                    <div className='album-expiration-date-container'>
                        <FontAwesomeIcon icon={faCalendar} /> <div>{startDate} - {endDate}</div>
                    </div>
                </div>
                <div className='album-control-panel-options'>
                    <button>Выделить фото</button>
                    <button>Выделить всё</button>
                    <button>Скачать</button>
                    <FontAwesomeIcon icon={faFilter} />
                </div>
            </div>

            {/* Список пользователей и их фото */}
            {members.map(member => (
                <div key={member.id} className="member-block">
                    <div className='friend-data-container-wrapper'>
                        <div className='friend-data-container'>
                            <img className='friend-avatar' src={member.photo}/>
                            <div className='friend-name'>{member.login}</div>
                        </div>
                        <div className='friend-select-all-container'>
                            <label htmlFor={`select-${member.id}`}>Выделить всё</label>
                            <input type="checkbox" id={`select-${member.id}`} />
                        </div>
                    </div>

                    <div className='friend-photo-container'>
                        {(photosByUser[member.id] || Array(10).fill(null)).map((photo, idx) => {
                            const isSelected = photo && selectedPhotos.has(photo.id);
                            return (
                                <div 
                                    key={idx} 
                                    className={`friend-photo ${isSelected ? 'selected' : ''}`}
                                    onClick={() => photo && togglePhotoSelection(photo.id)}
                                    style={{ 
                                        cursor: photo ? 'pointer' : 'default',
                                        position: 'relative'
                                    }}
                                >
                                    {photo && (
                                        <img 
                                            src={photo.image} 
                                            alt="upload" 
                                            style={{ width: '100%', height: '100%', objectFit: 'cover' }} 
                                        />
                                    )}
                                </div>
                            );
                        })}
                    </div>
                </div>
            ))}
        </div>
    );
}



