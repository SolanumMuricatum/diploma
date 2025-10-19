import React, { useState } from 'react';
import { toast } from 'react-toastify';
import '../styles/main.css';
import { useAuth } from '../auth/AuthProvider';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from '@fortawesome/free-solid-svg-icons'

export const Main = () => {
  return (
    <div className='main-container' >
        <div className='main-photoalbum-container'>
            <p>------ Фотоальбомы ------</p>
            <div>
                <button>Созданные</button>
                <button>Добавленные</button>
            </div>
        </div>
    </div>
  );
};
