import React, { useEffect } from 'react';
import { toast } from 'react-toastify';
import '../styles/photosOfUserToUpload.css';
import { useAuth } from '../auth/AuthProvider';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from '@fortawesome/free-solid-svg-icons'
import { Link } from 'react-router-dom';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { faUpload } from '@fortawesome/free-solid-svg-icons';
import { useParams } from 'react-router-dom'; 

export const PhotosOfUserToUpload = ({slots, setSlots}) => {
    const { userId, setUserId, setUserLogin } = useAuth();
    const { albumId } = useParams();

    useEffect(() => {
        const initPhotos = async () => {
            if (!albumId || !userId) return; // Ждем, пока параметры будут доступны

            const photosFromServer = await getAllPhotos();
            
            if (photosFromServer && photosFromServer.length > 0) {
                const newSlots = Array(10).fill(null);
                
                photosFromServer.forEach((photo, index) => {
                    if (index < 10) {
                        newSlots[index] = {
                            id: photo.id,
                            preview: photo.image, // Твой Base64 из базы
                            isNew: false,
                            fromServer: true
                        };
                    }
                });
                setSlots(newSlots);
            }
        };

        initPhotos();
    }, [albumId, userId]); // Сработает, когда появятся ID


    const toBase64 = (file) => new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
    });

    const handleFileChange = async (index, e) => {
        const file = e.target.files[0];
        if (file) {
            try {
                // Конвертируем в base64
                const base64Image = await toBase64(file);

                // Сразу вызываем сохранение
                const success = await postPhoto(base64Image);

                if (success) {
                    const newSlots = [...slots];
                    newSlots[index] = {
                        file: file,
                        preview: URL.createObjectURL(file),
                        isNew: false // уже сохранено
                    };
                    setSlots(newSlots);
                }
            } catch (error) {
                console.error("Ошибка при обработке файла:", error);
            }
        }
    };

    const getAllPhotos = async () => {
        try {
            // Исправлен синтаксис query-параметров (?key=value&key2=value2)
            const response = await fetch(`http://localhost:8080/album/get/all/photo?albumId=${albumId}&userId=${userId}`, {
                method: 'GET',
                credentials: 'include',
            });

            if (response.ok) {
                const data = await response.json(); // Получаем список объектов [{image: "..."}, ...]
                return data;
            }
            return [];
        } catch (error) {
            console.error('Ошибка при подгрузке фото:', error);
            return [];
        }
    };


    const postPhoto = async (base64String) => {
        try {
            const response = await fetch(`http://localhost:8080/album/save/photo`, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                // Отправляем объект с base64 строкой
                body: JSON.stringify({
                    image: base64String, 
                    albumId: albumId, 
                    userId: userId 
                }),
            });

            if (response.status === 409) {
                setUserId(null);
                setUserLogin(null);
                return false;
            }

            if (response.ok) {
                alert('Фото успешно добавлено!');
                return true;
            }
        } catch (error) {
            console.error('Ошибка сети:', error);
            return false;
        }
    };

    const handleDelete = async (index) => {
        const isConfirmed = window.confirm("Вы уверены, что хотите удалить это фото?");

        if (!isConfirmed) return;
        
        const photoToDelete = slots[index];

        if (photoToDelete?.id) {
            const success = await deletePhoto(photoToDelete.id);
            if (!success) {
                alert("Не удалось удалить фото с сервера");
                return;
            }
        }

        // Очищаем локальный стейт
        const newSlots = [...slots];
        if (newSlots[index]?.preview && !newSlots[index].id) {
            // Если это был временный Blob (для новых), чистим память
            URL.revokeObjectURL(newSlots[index].preview);
        }
        newSlots[index] = null;
        setSlots(newSlots);
    };

    const deletePhoto = async (photoId) => {
        try {
            const response = await fetch(`http://localhost:8080/album/delete/photo?photoId=${photoId}`, {
                method: 'DELETE',
                credentials: 'include',
            });

            if (response.status === 409) {
                setUserId(null);
                setUserLogin(null);
                return false;
            }

            alert("Фото успешно удалено!")
            return response.ok;
        } catch (error) {
            console.error('Ошибка при удалении:', error);
            return false;
        }
    };

  const handleSave = async () => {
    const formData = new FormData();
    // Собираем только те, что юзер добавил сейчас
    slots.forEach((slot, i) => {
      if (slot?.isNew) formData.append(`photo_${i}`, slot.file);
    });
    
    console.log('Отправляем FormData на сервер...');
    // await fetch('/api/user/photos', { method: 'POST', body: formData });
  };

  return (
    <div className='main-photos-to-upload-container' style={{position: 'relative', zIndex: '2'}}>
      <div className='photos-to-upload-container'>
        {slots.map((slot, index) => (
          <div key={index} className='photos-to-upload-photo-container'>
            {slot ? ( 
                <>
                    <div 
                        onClick={() => handleDelete(index)}
                        style={{
                            position: 'absolute',
                            width: '30px',
                            height: '30px',
                            top: '5px',
                            right: '5px',
                            backgroundColor: 'white',
                            borderRadius: '50%',
                            border: '1px solid black',
                            cursor: 'pointer',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center'
                        }}
                    >
                        <FontAwesomeIcon icon={faTrash} />
                    </div>
                    <img src={slot.preview || slot.urlFromServer} alt="user" style={{ width: '100%', height: '100%', objectFit: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center'}} />
                </>
            ) : (
              <label style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%', cursor: 'pointer' }}>
                <input type="file" hidden onChange={(e) => handleFileChange(index, e)} />
                <span style={{ fontSize: '24px' }}>
                    <FontAwesomeIcon icon={faUpload} />
                </span>
              </label>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
