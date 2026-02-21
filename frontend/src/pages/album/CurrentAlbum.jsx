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
import JSZip from 'jszip';
import { saveAs } from 'file-saver';

export function CurrentAlbum() {
    const { albumId } = useParams();
    const { parent } = useParams();
    const [members, setMembers] = useState([]);
    const [photosByUser, setPhotosByUser] = useState({}); // { userId: [photo1, photo2, ...] }
    const [selectedPhotos, setSelectedPhotos] = useState(new Set()); // Храним ID выбранных фото
    const [creator, setCreator] = useState();
    const [startDate, setStartDate] = useState();
    const [endDate, setEndDate] = useState();
    const [isSelectionMode, setIsSelectionMode] = useState(false);
    const [isDownloading, setIsDownloading] = useState(false);
    const [albumName, setAlbumName] = useState([]);
    const [currentPhotoIdx, setCurrentPhotoIdx] = useState(null);

    const allPhotosFlat = Object.values(photosByUser)
    .flat()
    .filter(p => p !== null && p.image);

    const showNext = (e) => {
        e.stopPropagation();
        setCurrentPhotoIdx((prev) => (prev + 1) % allPhotosFlat.length);
    };

    const showPrev = (e) => {
        e.stopPropagation();
        setCurrentPhotoIdx((prev) => (prev - 1 + allPhotosFlat.length) % allPhotosFlat.length);
    };


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
        setSelectedPhotos(prev => {
            const newSet = new Set(prev);
            newSet.has(photoId) ? newSet.delete(photoId) : newSet.add(photoId);
            return newSet;
        });
    };

    const handleSelectAll = (memberId, isChecked) => {
        const userSlots = photosByUser[memberId] || [];
        // Собираем ID только тех фото, которые реально существуют (не null)
        const loadedPhotoIds = userSlots
            .filter(p => p !== null && p.id !== undefined)
            .map(p => p.id);
        
        if (loadedPhotoIds.length === 0) return;

        const newSelection = new Set(selectedPhotos);
        
        loadedPhotoIds.forEach(id => {
            if (isChecked) {
                newSelection.add(id);
            } else {
                newSelection.delete(id);
            }
        });
        
        setSelectedPhotos(newSelection);
    };

    const handleToggleAllPhotos = () => {
        // 1. Собираем ID абсолютно всех загруженных фото всех пользователей
        const allLoadedIds = Object.values(photosByUser)
            .flat() // превращаем объект массивов в один плоский массив
            .filter(p => p !== null && p.id) // убираем пустые слоты
            .map(p => p.id);

        // 2. Проверяем, выбраны ли уже все эти фото
        const isAllSelected = allLoadedIds.length > 0 && selectedPhotos.size === allLoadedIds.length;

        if (isAllSelected) {
            // Если всё выделено — сбрасываем в пустой Set
            setSelectedPhotos(new Set());
        } else {
            // Если не всё — выделяем всё
            setSelectedPhotos(new Set(allLoadedIds));
            // Заодно включим режим выделения, чтобы появилась обводка
            setIsSelectionMode(true); 
        }
    };

    const handleDownloadSelected = async () => {
        console.log("started");
        if (selectedPhotos.size === 0) return;

        setIsDownloading(true); // Включаем "крутилку"
        const zip = new JSZip();
        
        // Собираем массив объектов фото, которые выбраны
        const photosToDownload = Object.values(photosByUser)
            .flat()
            .filter(p => p && selectedPhotos.has(p.id));

        try {
            const downloadPromises = photosToDownload.map(async (photo) => {
                const response = await fetch(photo.image, {
                    method: 'GET',
                    mode: 'cors',
                    cache: 'no-cache', // Игнорируем "плохой" кэш без заголовков
                });
                const blob = await response.blob();
                // Используем имя из URL или ID
                const fileName = photo.image.split('/').pop() || `photo_${photo.id}.jpg`;
                zip.file(fileName, blob);
            });

            await Promise.all(downloadPromises);
            
            const content = await zip.generateAsync({ type: "blob" });
            saveAs(content, `${albumName}.zip`);
        } catch (err) {
            console.error("Ошибка при создании архива:", err);
            alert("Не удалось скачать некоторые фото :(");
        } finally {
            setIsDownloading(false); // Выключаем лоадер в любом случае
        }
    };

    useEffect(() => {
        console.log("Выбранные фото:", Array.from(selectedPhotos));
    }, [selectedPhotos]);

    useEffect(() => {
        const handleKeyDown = (e) => {
            // Если модалка закрыта — ничего не делаем
            if (currentPhotoIdx === null) return;

            if (e.key === 'ArrowRight') {
                showNext(e);
            } else if (e.key === 'ArrowLeft') {
                showPrev(e);
            } else if (e.key === 'Escape') {
                setCurrentPhotoIdx(null); // Прямой вызов функции закрытия
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        
        // Обязательно чистим обработчик при размонтировании
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [currentPhotoIdx, allPhotosFlat.length]); // Следим за индексом и длиной массива


    return (
        <div style={{ position: 'relative' }}>
            <Link to={`/albums/${parent}`}>
                <div className='album-arrow-right-container'><FontAwesomeIcon icon={faArrowLeft} /></div>
            </Link>
            
            <Album setAlbumName={setAlbumName} setCreator={setCreator} setStartDate={setStartDate} setEndDate={setEndDate} />

            {/* Панель управления (теперь кнопки могут что-то делать) */}
            <div className='album-control-panel'>
                <div className='album-creator-container'>
                    <FontAwesomeIcon icon={faUser} /> <div>{creator}</div>
                    <div className='album-expiration-date-container'>
                        <FontAwesomeIcon icon={faCalendar} /> <div>{startDate} - {endDate}</div>
                    </div>
                </div>
                <div className='album-control-panel-options'>
                    <button onClick={handleToggleAllPhotos}>
                        Выделить всё
                    </button>
                    <button 
                        onClick={handleDownloadSelected} 
                        disabled={selectedPhotos.size === 0 || isDownloading}
                        className={isDownloading ? 'btn-loading' : ''}
                    >
                        {isDownloading ? (
                            <>Готовим архив... <span className="loader-dots"></span></>
                        ) : (
                            `Скачать (${selectedPhotos.size})`
                        )}
                    </button>
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
                            <input 
                                type="checkbox" 
                                id={`select-${member.id}`}
                                checked={(() => {
                                    const photos = (photosByUser[member.id] || []).filter(p => p !== null);
                                    return photos.length > 0 && photos.every(p => selectedPhotos.has(p.id));
                                })()}
                                onChange={(e) => handleSelectAll(member.id, e.target.checked)}
                            />
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
                                    onDoubleClick={() => {
                                        if (photo) {
                                            const index = allPhotosFlat.findIndex(p => p.id === photo.id);
                                            setCurrentPhotoIdx(index);
                                        }
                                    }}
                                    style={{ cursor: 'pointer', position: 'relative', userSelect: 'none' }}
                                >
                                    {photo && (
                                        <img 
                                            src={photo.image} 
                                            alt="upload" 
                                            draggable="false"
                                            style={{ width: '100%', height: '100%', objectFit: 'cover' }} 
                                        />
                                    )}
                                </div>
                            );
                        })}
                    </div>
                </div>
            ))}
            {currentPhotoIdx !== null && allPhotosFlat[currentPhotoIdx] && (
                <div className="photo-modal-overlay" onClick={() => setCurrentPhotoIdx(null)}>
                    <button className="modal-close-btn" onClick={() => setCurrentPhotoIdx(null)}>&times;</button>
                    
                    <button className="modal-nav-btn prev" onClick={showPrev}>❮</button>
                    
                    <div className="modal-image-container" onClick={(e) => e.stopPropagation()}>
                        <img 
                            src={allPhotosFlat[currentPhotoIdx].image} 
                            alt="Full size" 
                        />
                    </div>

                    <button className="modal-nav-btn next" onClick={showNext}>❯</button>
                </div>
            )}
        </div>
    );
}



