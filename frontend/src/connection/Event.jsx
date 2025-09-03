import '../styles/event.css';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';

export const Event = () => {
    const [event, setEvent] = useState(null);
    const [err, setErr] = useState(false);
    const { eventId } = useParams();

    useEffect(() => {
        setEvent(null); // сброс
        setErr(false);  // сброс ошибки
        const fetchEvent = async () => {
            try {
                let response = await fetch(`http://localhost:8080/event/get?id=${eventId}`);
                if (!response.ok) {
                    throw new Error();
                }
                let data = await response.json(); 
                setEvent(data);
            } catch (error) {
                setErr(true);
                console.error('Ошибка при получении события:', error);
            }
        };

        fetchEvent();
    }, [eventId]);

    if (err) {
        return (
            <div>
                <div className='event-title'>Ошибка доступа к мероприятию</div>
            </div>
        );
    }

    if (!event) {
        return (
            <div>
                <div className='event-title'>Загрузка...</div>
            </div>
        );
    }

    return (
        <div>
            <div className='event-title' style={{backgroundImage: `url(${event.background})`, color: `${event.textColor}`, fontFamily: `${event.textFont === "None" ? `-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji"` : `${event.textFont}` }`, fontSize: `${event.textSize}pt`}}>{event.name}</div>
            <div className='event-description'>
                <div>{`Создатель: ${event.creatorName} ${event.creatorSurname}`}</div>
                <div>{`Дата создания: ${event.startDate}`}</div>
                <div>{`Дата окончания: ${event.endDate}`}</div> 
            </div>
        </div>
    );
};
