import '../styles/createEvent.css';
import bg1 from '../photo/event/backgrounds/1.png';
import bg2 from '../photo/event/backgrounds/2.png';
import bg3 from '../photo/event/backgrounds/3.png';
import bg4 from '../photo/event/backgrounds/4.png';
import bg5 from '../photo/event/backgrounds/5.png';
import bg6 from '../photo/event/backgrounds/6.png';
import bg7 from '../photo/event/backgrounds/7.png';
import bg8 from '../photo/event/backgrounds/8.png';
import bg9 from '../photo/event/backgrounds/9.png';
import bg10 from '../photo/event/backgrounds/10.png';
import bg11 from '../photo/event/backgrounds/11.png';
import bg12 from '../photo/event/backgrounds/12.png';
import bg13 from '../photo/event/backgrounds/13.png';
import bg14 from '../photo/event/backgrounds/14.png';
import bg15 from '../photo/event/backgrounds/15.png';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { ReactComponent as CameraIcon } from '../photo/photo-camera-svgrepo-com.svg';
import { ReactComponent as PhotoIcon } from '../photo/picture-svgrepo-com.svg';
import { faArrowUp, faDirections } from '@fortawesome/free-solid-svg-icons'
import React, { useEffect, useState } from 'react';
import { Event } from '../connection/Event';
import { useParams } from 'react-router-dom'; // Убедитесь, что это правильно
import { useAuth } from '../auth/AuthProvider';

export function EventPage() {
    const { eventId } = useParams();
    const [disableButton, setDisableButton] = useState(true);
    const [err, setErr] = useState(false);
    const [event, setEvent] = useState({ name: '', background: '', textColor: '#000000', textFont: 'None', textSize: ''});
    const { userId, setUserId } = useAuth();
    //const userId  = '68994ca2-998a-48db-9696-2e7bc761b977';
    const [backgroundBorderColor, setBackgroundBorderColor] = useState(null);
    const [borderNameColor, setBorderNameColor] = useState(null);
    const backgrounds = [bg1, bg2, bg3, bg4, bg5, bg6, bg7, bg8, bg9, bg10, bg11, bg12, bg13, bg14, bg15];

    useEffect(() => {
        if(eventId != undefined){
            const getEvent = async () => {
                try {
                    let response = await fetch(`http://localhost:8080/event/get?id=${eventId}`, {
                        method: 'GET',
                        headers: { 'Content-Type': 'application/json' },
                        credentials: 'include',
                    });
                    if (!response.ok) {
                        if(response == 409){
                            setUserId(null);
                        }
                        throw new Error('Network response was not ok');
                    }
                    let data = await response.json(); // Получаем данные только один раз
                    setEvent(data);
                    setDisableButton(false);
                } catch (error) {
                    setErr(true);
                    console.error('Ошибка при получении события:', error);
                }
            };
            getEvent();
        }
    }, []);

    const handleInputChange = (e) => {
        const { value } = e.target;
        setEvent((prevEvent) => ({
        ...prevEvent,
        ['name']: value,
        }));
    };

    const handleTextColorClick = (index) => {
        setBorderNameColor(index); //just it to be existed
        setEvent((prevEvent) => ({
        ...prevEvent,
        ['textColor']: textColors[index],
        }));
    };

    const handleBackgroundClick = (bgd) => {
        setBackgroundBorderColor(bgd); //just it to be existed
        setEvent((prevEvent) => ({
        ...prevEvent,
        ['background']: bgd,
        }));
    };

    const handleTextFontCLick = (font) => {
        setEvent((prevEvent) => ({
             ... prevEvent,
            ['textFont']: font,
        }));
    }

    const  handletextSizeClick = (value) =>{
        console.log(value);
        setEvent((prevEvent) => ({
        ...prevEvent,
        ['textSize']: value,
        }));
        console.log(eventId);
    }

    const postEvent = async () => {
        try {
            const response = await fetch(`http://localhost:8080/event/save?creatorId=${userId}`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({name: event.name, background: event.background, textColor: event.textColor, textFont: event.textFont, textSize: event.textSize}),
            });

            const statusCode = response.status;
            if(statusCode == 409){
                setUserId(null);
            }
            console.log('Код ответа:', statusCode);
            alert('Событие успешно создано!');
            window.location.reload();

            return statusCode;
        } catch (error) {
            console.error('Ошибка при отправке данных на сервер:', error); // Логируем ошибку
            return { error: 'Произошла ошибка при соединении с сервером' };
        }
    };

    const putEvent = async () => {
        try {
            const response = await fetch(`http://localhost:8080/event/update?eventId=${eventId}`, {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({name: event.name, background: event.background, textColor: event.textColor, textFont: event.textFont, textSize: event.textSize}),
            });

            const statusCode = response.status;
            if(statusCode == 409){
                setUserId(null);
            }
            console.log('Код ответа:', statusCode);
            alert('Событие успешно отредактировано!');
            window.location.reload();

            return statusCode;
        } catch (error) {
            console.error('Ошибка при отправке данных на сервер:', error); // Логируем ошибку
            return { error: 'Произошла ошибка при соединении с сервером' };
        }
    };

    const textColors = [
        "#000000",
        "#800000",
        "#FA8072",
        "#FFE4C4",
        "#2E8B57",
        "#008B8B",
        "#000080",
        "#EE82EE",
        "#DB7093",
        "#ffffff"
    ];

    const textFonts = [
        "None",
        "Old Classic",
        "Isabella-Decor",
        "Rosarium",
        "Manrope"
    ];
    
    return (
        <div>
            {err && (
                <div>
                    <div className='event-create-title'>
                        Ошибка доступа к мероприятию
                    </div>
                </div>
            )}
            {!err && (
                <div>
                    <div className='event-create-title' style={{backgroundImage: `url(${event.background})`, color: `${event.textColor}`, fontFamily: `${event.textFont === "None" ? `-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji"` : `${event.textFont}` }`, fontSize: `${event.textSize}pt`}}>
                        {event.name}
                    </div>
                </div>
            )}
            <div className='event-constructor'>
                <div className='first-block-create-event'>
                    <div class="event-name-input">
                        <label for="input" class="text">Название:</label>
                        <input onChange={handleInputChange} type="text" placeholder="Пишите здесь..." name="input" value={event.name} class="input" maxLength={50}/>
                    </div> 
                    <div className='background-block-create-event'>
                        <div>Выберите фон</div>
                        <div className="background-create-event">
                            {backgrounds.map((bgd) => (
                                <div
                               // key={id}
                               //id={`${id}-background-create-event`}
                                onClick={() => handleBackgroundClick(bgd)}
                                style={{
                                    outline: `3px solid ${backgroundBorderColor === bgd || event.background === bgd ? '#112250' : 'transparent'}`,
                                    border:  `3px solid #112250`,
                                    outlineOffset: `3px`,
                                    backgroundImage: `url(${bgd})`,
                                    backgroundRepeat: `no-repeat`,
                                    backgroundSize: `cover`,
                                    backgroundPosition: `center`
                                }}
                                ></div>
                            ))}
                        </div>
                    </div>
                </div>
                <div className='second-block-create-event'>
                    <div className='text-color-block-create-event'>
                        <div>Выберите цвет текста</div>
                        <div className="text-color-create-event">
                            {textColors.map((color, index) => (
                                <div
                                onClick={() => handleTextColorClick(index)}
                                style={{
                                    outline: `3px solid ${borderNameColor === index || event.textColor === color ? '#112250' : 'transparent'}`,
                                    border: `3px solid #112250`,
                                    outlineOffset: `3px`,
                                    backgroundColor: `${color}`
                                }}
                                ></div>
                            ))}
                        </div>
                    </div>
                    <div classname='text-font-block-create-event'>
                        <div>Выберите шрифт</div>
                        <div className="text-font-create-event">
                            {textFonts.map((font) => (
                            <div>
                                {event.textFont === font && (
                                    <input style={{accentColor: '#112250'}} type="radio" id={font} name="font" value={font} defaultChecked onChange={() => handleTextFontCLick(font)} />
                                )}
                                {event.textFont != font && (
                                    <input style={{accentColor: '#112250'}} type="radio" id={font} name="font" value={font} onChange={() => handleTextFontCLick(font)} />
                                )}
                                <label for={font}>{font}</label>
                            </div>
                            ))}
                        </div>
                    </div>
                </div>
                <div className='third-block-create-event'>
                    <div>Выберите размер шрифта</div>
                    <div>
                        <input style={{accentColor: '#112250'}} type="range" id="volume" name='volume' min="28" max="60" defaultValue={event.textSize} onChange={(e) => handletextSizeClick(e.target.value)} />
                    </div>
                    {(eventId && !err) && (
                        <div>
                            <button onClick={eventId != null ? () => putEvent() : () => postEvent() }>Save</button>
                        </div> 
                    )}
                    {!eventId && (
                        <div>
                            <button onClick={eventId != null ? () => putEvent() : () => postEvent() }>Save</button>
                        </div> 
                    )}
                </div>
            </div>
             
        </div>
    );
}


