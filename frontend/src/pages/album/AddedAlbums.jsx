import '../../styles/addedAlbums.css';
import { Event } from '../../connection/Album'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { ReactComponent as CameraIcon } from '../../photo/photo-camera-svgrepo-com.svg';
import { ReactComponent as PhotoIcon } from '../../photo/picture-svgrepo-com.svg';
import { faArrowUp, faDirections } from '@fortawesome/free-solid-svg-icons'
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'
import React, { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; // Убедитесь, что это правильно
import { faEllipsisVertical } from '@fortawesome/free-solid-svg-icons'
import { faCalendar } from '@fortawesome/free-solid-svg-icons'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faPlus } from '@fortawesome/free-solid-svg-icons'
import { useAuth } from '../../auth/AuthProvider';
import { Link } from 'react-router-dom';

export function AddedAlbums() {
    const { userId } = useAuth();
    const navigate = useNavigate();
    const [albums, setAlbums] = useState(null);
    const [err, setErr] = useState(false);
    const { albumId } = useParams();
    
    const [openMenuId, setOpenMenuId] = useState(null); 
    const menuRef = useRef(null);

    useEffect(() => {
        if (!userId) return; 
        setAlbums(null);
        setErr(false);
        const fetchEvent = async () => {
            try {
                let response = await fetch(`http://localhost:8080/album/get/all/added?userId=${userId}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: 'include',
                });
                if (!response.ok) throw new Error();
                let data = await response.json(); 
                setAlbums(data);
            } catch (error) {
                setErr(true);
                console.error('Ошибка:', error);
            }
        };
        fetchEvent();
    }, [userId]);

    return (
        <div className='added-albums-page-container'>
            <Link to="/main">
                <div className='added-albums-arrow-right-container'>
                    <FontAwesomeIcon icon={faArrowLeft}/>
                </div>
            </Link>
            <div className='added-albums-container'>
                <div style={{ marginBottom: '100px', fontSize: '18pt' }}>
                    --- Добавленные фотоальбомы ---
                </div>
                
                <div className='added-albums-wrapper'>
                    {err && <div>Ошибка загрузки альбомов</div>}
                    {albums && albums.length === 0 && <div>У вас пока нет созданных альбомов.</div>}
                    {!albums && !err && <div>Загрузка...</div>}

                    {albums && Array.isArray(albums) && albums.map((album) => {
                        const isMenuOpen = openMenuId === album.id;
                        return (
                            <Link to={`/albums/added/${album.id}`} key={album.id}>
                                <div className='added-album-card-wrapper'>
                                    <div className='added-album-card'>
                                        <div className='added-album-image-container'>
                                            <div className='added-album-image' style={{backgroundImage: `url(${album.background})`}}></div>
                                        </div>
                                        <div style={{ position: 'relative' }}>
                                            <div className='added-album-title'>{album.name}</div>
                                            <div className='added-album-creator'>
                                                <FontAwesomeIcon icon={faUser} /> {album.creatorLoginSnapshot}
                                            </div>
                                            <div className='added-album-date'>
                                                <FontAwesomeIcon icon={faCalendar} /> {album.startDate} - {album.endDate}
                                            </div>

                                            <div className='added-album-options-icon' 
                                                onClick={(e) => {
                                                    e.preventDefault(); 
                                                    e.stopPropagation(); 
                                                    setOpenMenuId(isMenuOpen ? null : album.id);
                                                }}
                                                style={{ padding: '10px', cursor: 'pointer' }}
                                            >
                                                <FontAwesomeIcon icon={faEllipsisVertical} />
                                            </div>

                                            {isMenuOpen && (
                                                <div 
                                                    className='added-album-options' 
                                                    ref={menuRef} 
                                                    onClick={(e) => e.stopPropagation()} 
                                                >
                                                    <div className='menu-item' onClick={(e) => { e.preventDefault(); navigate(`/albums/manage/photos/${album.id}`);}}>Управлять фотографиями</div>
                                                    <div className='menu-item' onClick={(e) => { e.stopPropagation(); console.log('Delete'); }}>Покинуть фотоальбом</div>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            </Link>
                        );
                    })}
                </div>
            </div>
            <div className='added-album-add-button'>
                <Link to={`/albums/create`}>
                    <FontAwesomeIcon icon={faPlus} />
                </Link>
            </div>
        </div>
    );
}
