import '../styles/event.css';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';

export const Event = () => {
    const [event, setEvent] = useState('');
    const { eventId } = useParams(); // Получаем id из параметров маршрута
    useEffect(() => {
        setEvent(null); // Очищаем старые данные
        const fetchEvent = async () => {
            try {
                let response = await fetch(`http://localhost:8080/event/get?id=${eventId}`);
                // if (!response.ok) {
                //     throw new Error('Network response was not ok');
                // }
                let data = await response.json(); 
                setEvent(data);
            } catch (error) {
                console.error('Ошибка при получении события:', error);
            }
        };

        fetchEvent();
    }, [eventId]); // Передаем eventId как зависимость

  return (
    <div>
        <div className='event-title'>
            {event ? event.name : 'Ошибка доступа к меропиятию'}
        </div>
        <div className='event-description'> {event ? `Создатель: ${event.creatorName} ${event.creatorSurname}    Дата создания: ${event.startDate}    Дата окончания: ${event.endDate}` : `` } </div>
    </div>
  );
};
