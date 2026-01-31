import '../styles/album.css';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';

export const Album = ({setCreator, setStartDate, setEndDate}) => {
    const [album, setAlbum] = useState(null);
    const [err, setErr] = useState(false);
    const { albumId } = useParams();

    useEffect(() => {
        setAlbum(null); // сброс
        setErr(false);  // сброс ошибки
        const fetchEvent = async () => {
            try {
                let response = await fetch(`http://localhost:8080/album?id=${albumId}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: 'include',
                });
                if (!response.ok) {
                    throw new Error();
                }
                let data = await response.json(); 
                console.log(data);
                setAlbum(data);

                setCreator(data.creatorLoginSnapshot);
                setStartDate(data.startDate);
                setEndDate(data.endDate);
            } catch (error) {
                setAlbum({creatorLoginSnapshot: 'pepino'})
                setErr(true);
                console.error('Ошибка при получении события:', error);
            }
        };

        fetchEvent();
        
    }, [albumId]);

    if (err) {
        return (
            <div>
                <div className='album-title'>Ошибка доступа к мероприятию</div>
            </div>
        );
    }

    if (!album) {
        return (
            <div>
                <div className='album-title'>Загрузка...</div>
            </div>
        );
    }

    return (
        <div>
            <div className='album-title' style={{backgroundImage: `url(${album.background})`, color: `${album.textColor}`, fontFamily: `${album.textFont}`, fontSize: `${album.textSize}pt`}}>{album.name}</div>
{/*             <div className='album-description'>
                <div>{`Создатель: ${album.creatorLoginSnapshot}`}</div>
                <div>{`Дата создания: ${album.startDate}`}</div>
                <div>{`Дата окончания: ${album.endDate}`}</div> 
            </div> */}
        </div>
    );
};
