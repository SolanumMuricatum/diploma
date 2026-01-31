import '../../styles/createUpdateAlbum.css';
import bg1 from '../../photo/event/backgrounds/1.png';
import bg2 from '../../photo/event/backgrounds/2.png';
import bg3 from '../../photo/event/backgrounds/3.png';
import bg4 from '../../photo/event/backgrounds/4.png';
import bg5 from '../../photo/event/backgrounds/5.png';
import bg6 from '../../photo/event/backgrounds/6.png';
import bg7 from '../../photo/event/backgrounds/7.png';
import bg8 from '../../photo/event/backgrounds/8.png';
import bg9 from '../../photo/event/backgrounds/9.png';
import bg10 from '../../photo/event/backgrounds/10.png';
import bg11 from '../../photo/event/backgrounds/11.png';
import bg12 from '../../photo/event/backgrounds/12.png';
import bg13 from '../../photo/event/backgrounds/13.png';
import bg14 from '../../photo/event/backgrounds/14.png';
import bg15 from '../../photo/event/backgrounds/15.png';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { ReactComponent as CameraIcon } from '../../photo/photo-camera-svgrepo-com.svg';
import { ReactComponent as PhotoIcon } from '../../photo/picture-svgrepo-com.svg';
import { faArrowUp, faDirections } from '@fortawesome/free-solid-svg-icons'
import React, { useEffect, useState } from 'react';
import { Event } from '../../connection/Album';
import { useParams } from 'react-router-dom'; // Убедитесь, что это правильно
import { useAuth } from '../../auth/AuthProvider';

export function CreateUpdateAlbum() {
    const { albumId } = useParams();
    const [disableButton, setDisableButton] = useState(true);
    const [err, setErr] = useState(false);
    const [album, setAlbum] = useState({ name: '', background: '', textColor: '#000000', textFont: 'None', textSize: '44'});
    const { userId, setUserId } = useAuth();
    const { userLogin, setUserLogin } = useAuth();
    //const userId  = '68994ca2-998a-48db-9696-2e7bc761b977';
    const [backgroundBorderColor, setBackgroundBorderColor] = useState(null);
    const [borderNameColor, setBorderNameColor] = useState(null);
    const backgrounds = [bg1, bg2, bg3, bg4, bg5, bg6, bg7, bg8, bg9, bg10, bg11, bg12, bg13, bg14, bg15];

    useEffect(() => {
        if(albumId != undefined){
            const getAlbum = async () => {
                try {
                    let response = await fetch(`http://localhost:8080/album?id=${albumId}`, {
                        method: 'GET',
                        headers: { 'Content-Type': 'application/json' },
                        credentials: 'include',
                    });
                    if (!response.ok) {
                        if(response == 409){
                            setUserId(null);
                            setUserLogin(null);
                        }
                        throw new Error('Network response was not ok');
                    }
                    let data = await response.json(); // Получаем данные только один раз
                    setAlbum(data);
                    setDisableButton(false);
                } catch (error) {
                    setErr(true);
                    console.error('Ошибка при получении события:', error);
                }
            };
            getAlbum();
        }
    }, []);

    const handleInputChange = (e) => {
        const { value } = e.target;
        setAlbum((prevAlbum) => ({
        ...prevAlbum,
        ['name']: value,
        }));
    };

    const handleTextColorClick = (index) => {
        setBorderNameColor(index); //just it to be existed
        setAlbum((prevAlbum) => ({
        ...prevAlbum,
        ['textColor']: textColors[index],
        }));
    };

    const handleBackgroundClick = (bgd) => {
        console.log("mc kmd ckdckwemcowemxow " + bgd + "login" + userLogin)
        setBackgroundBorderColor(bgd); //just it to be existed
        setAlbum((prevAlbum) => ({
        ...prevAlbum,
        ['background']: bgd,
        }));
    };

    const handleTextFontCLick = (font) => {
        setAlbum((prevAlbum) => ({
             ... prevAlbum,
            ['textFont']: font,
        }));
    }

    const  handletextSizeClick = (value) =>{
        console.log(value);
        setAlbum((prevAlbum) => ({
        ...prevAlbum,
        ['textSize']: value,
        }));
        console.log(albumId);
    }

    const postAlbum = async () => {
        try {
            const response = await fetch(`http://localhost:8080/album/save`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({name: album.name, background: album.background, textColor: album.textColor, textFont: album.textFont, textSize: album.textSize, creatorId: userId, creatorLoginSnapshot: userLogin}),
            });

            const statusCode = response.status;
            if(statusCode == 409){
                setUserId(null);
                setUserLogin(null);
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

    const putAlbum = async () => {
        try {
            const response = await fetch(`http://localhost:8080/album/update?albumId=${albumId}`, {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({name: album.name, background: album.background, textColor: album.textColor, textFont: album.textFont, textSize: album.textSize, creatorId: userId, creatorLoginSnapshot: userLogin}),
            });

            const statusCode = response.status;
            if(statusCode == 409){
                setUserId(null);
                setUserLogin(null);
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
                    <div className='album-create-title'>
                        Ошибка доступа к мероприятию
                    </div>
                </div>
            )}
            {!err && (
                <div>
                    <div className='album-create-title' style={{backgroundImage: `url(${album.background})`, color: `${album.textColor}`, fontFamily: `${album.textFont === "None" ? `-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji"` : `${album.textFont}` }`, fontSize: `${album.textSize}pt`}}>
                        {album.name}
                    </div>
                </div>
            )}
            <div className='album-constructor'>
                <div className='first-block-create-album'>
                    <div class="album-name-input">
                        <label for="input" class="text">Название:</label>
                        <input onChange={handleInputChange} type="text" placeholder="Пишите здесь..." name="input" value={album.name} class="input" maxLength={50}/>
                    </div> 
                    <div className='background-block-create-album'>
                        <div>Выберите фон</div>
                        <div className="background-create-album">
                            {backgrounds.map((bgd) => (
                                <div
                               // key={id}
                               //id={`${id}-background-create-album`}
                                onClick={() => handleBackgroundClick(bgd)}
                                style={{
                                    outline: `3px solid ${backgroundBorderColor === bgd || album.background === bgd ? '#112250' : 'transparent'}`,
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
                <div className='second-block-create-album'>
                    <div className='text-color-block-create-album'>
                        <div>Выберите цвет текста</div>
                        <div className="text-color-create-album">
                            {textColors.map((color, index) => (
                                <div
                                onClick={() => handleTextColorClick(index)}
                                style={{
                                    outline: `3px solid ${borderNameColor === index || album.textColor === color ? '#112250' : 'transparent'}`,
                                    border: `3px solid #112250`,
                                    outlineOffset: `3px`,
                                    backgroundColor: `${color}`
                                }}
                                ></div>
                            ))}
                        </div>
                    </div>
                    <div className='text-font-block-create-album'>
                        <div>Выберите шрифт</div>
                        <div className="text-font-create-album">
                            {textFonts.map((font) => (
                            <div>
                                {album.textFont === font && (
                                    <input style={{accentColor: '#112250'}} type="radio" id={font} name="font" value={font} defaultChecked onChange={() => handleTextFontCLick(font)} />
                                )}
                                {album.textFont != font && (
                                    <input style={{accentColor: '#112250'}} type="radio" id={font} name="font" value={font} onChange={() => handleTextFontCLick(font)} />
                                )}
                                <label for={font}>{font}</label>
                            </div>
                            ))}
                        </div>
                    </div>
                </div>
                <div className='third-block-create-album'>
                    <div className='text-size-create-album' >
                        <div>Выберите размер шрифта</div>
                        <input style={{accentColor: '#112250', width: '300px', marginTop: '10px'}} type="range" id="volume" name='volume' min="28" max="60" defaultValue={album.textSize} onChange={(e) => handletextSizeClick(e.target.value)} />
                    </div>
                    {!err && (
                    <div className='button-container'>
                        <button onClick={albumId ? putAlbum : postAlbum}>
                            Сохранить
                        </button>
                    </div>
                    )}
                </div>
            </div>
             
        </div>
    );
}


