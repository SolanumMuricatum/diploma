import '../styles/hello.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { ReactComponent as CameraIcon } from '../photo/photo-camera-svgrepo-com.svg';
import { ReactComponent as PhotoIcon } from '../photo/picture-svgrepo-com.svg';
import { faArrowUp, faDirections } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';

export function Hello() {
    const navigate = useNavigate();
    const handleButtonClick = () =>{
        navigate('/login');
    }
    return (
        <div>
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',  marginTop: '50px', fontSize: '48pt', paddingTop: '125px', textAlign: 'center'}}>
                Поделись <br></br> воспоминаниями
                <button className='get-started-button' onClick={() => handleButtonClick()}>Начать</button>
                <div style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', marginTop: '50px', width: '20px', height: '20px',}}>
                    <FontAwesomeIcon className='arrow-get-started-icon' icon={faArrowUp}/>
{/*                 <div>Press to follow the instructions</div> */}
                </div>
            </div>
            {/* <div style={{position: 'absolute', zIndex: '100', left: '1150px', top: '320px', width: '150px', height: '150px', transform: 'rotate(35deg)'}}>
                <CameraIcon />
            </div> */}
            
            {/* <div style={{position: 'absolute', zIndex: '100', right: '1150px', top: '320px',  transform: 'rotate(-35deg)'}}>
                <PhotoIcon style={{width: '150px', height: '150px'}} />
            </div> */}

        </div>

    );
}


