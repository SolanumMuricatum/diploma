import '../../styles/createdAlbums.css';
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

export function CreatedAlbums() {
    const { userId } = useAuth();
    const navigate = useNavigate();
    const [albums, setAlbums] = useState(null);
    const [err, setErr] = useState(false);
    const { albumId } = useParams();
    
    // ИСПРАВЛЕНО: храним ID открытого меню, чтобы не открывались все сразу
    const [openMenuId, setOpenMenuId] = useState(null); 
    const menuRef = useRef(null);

/*     useEffect(() => {
        const handleClickOutside = (event) => {
            // Если меню открыто и клик был НЕ по иконке и НЕ по меню
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setOpenMenuId(null);
            }
        };
        // Используем 'click' вместо 'mousedown'
        document.addEventListener('click', handleClickOutside);
        return () => document.removeEventListener('click', handleClickOutside);
    }, []); */


    useEffect(() => {
        if (!userId) return; 
        setAlbums(null);
        setErr(false);
        const fetchEvent = async () => {
            try {
                let response = await fetch(`http://localhost:8080/album/get/all/created?creatorId=${userId}`, {
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
        <div className='created-albums-page-container'>
            <Link to="/main">
                <div className='created-albums-arrow-right-container'>
                    <FontAwesomeIcon icon={faArrowLeft}/>
                </div>
            </Link>
            <div className='created-albums-container'>
                <div style={{ marginBottom: '100px', fontSize: '18pt' }}>
                    --- Созданные фотоальбомы ---
                </div>
                
                <div className='created-albums-wrapper'>
                    {err && <div>Ошибка загрузки альбомов</div>}
                    {albums && albums.length === 0 && <div>У вас пока нет созданных альбомов.</div>}
                    {!albums && !err && <div>Загрузка...</div>}

                    {albums && Array.isArray(albums) && albums.map((album) => {
                        // Определяем, открыто ли меню именно для этой карточки
                        const isMenuOpen = openMenuId === album.id;

                        return (
                            <Link to={`/albums/created/${album.id}`} key={album.id}>
                                <div className='created-album-card-wrapper'>
                                    <div className='created-album-card'>
                                        <div className='created-album-image-container'>
                                            <div className='created-album-image' style={{backgroundImage: `url(${album.background})`}}></div>
                                        </div>
                                        <div style={{ position: 'relative' }}> {/* Контейнер для позиционирования меню */}
                                            <div className='created-album-title'>{album.name}</div>
                                            <div className='created-album-creator'>
                                                <FontAwesomeIcon icon={faUser} /> {album.creatorLoginSnapshot}
                                            </div>
                                            <div className='created-album-date'>
                                                <FontAwesomeIcon icon={faCalendar} /> {album.startDate} - {album.endDate}
                                            </div>

                                            <div className='created-album-options-icon' 
                                                onClick={(e) => {
                                                    e.preventDefault();  // Стоп переходу по ссылке
                                                    e.stopPropagation(); // Стоп всплытию к Link
                                                    //e.nativeEvent.stopImmediatePropagation(); 
                                                    setOpenMenuId(isMenuOpen ? null : album.id);
                                                }}
                                                style={{ padding: '10px', cursor: 'pointer' }}
                                            >
                                                <FontAwesomeIcon icon={faEllipsisVertical} />
                                            </div>

                                            {isMenuOpen && (
                                                <div 
                                                    className='created-album-options' 
                                                    ref={menuRef} 
                                                    onClick={(e) => e.stopPropagation()} 
                                                >
                                                    <div className='menu-item' onClick={(e) => {e.preventDefault(); navigate(`/albums/edit/${album.id}`);}}>Редактировать</div>
                                                    <div className='menu-item' onClick={(e) => { e.preventDefault(); navigate(`/albums/created/manage/photos/${album.id}`);}}>Управлять фотографиями</div>
                                                    <div className='menu-item' onClick={(e) => { e.stopPropagation(); console.log('Access'); }}>Управлять доступом</div>
                                                    <div className='menu-item delete' onClick={(e) => { e.stopPropagation(); console.log('Delete'); }}>Удалить</div>
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
            <div className='created-album-add-button'>
                <Link to={`/albums/create`}>
                    <FontAwesomeIcon icon={faPlus} />
                </Link>
            </div>
        </div>
    );
}
