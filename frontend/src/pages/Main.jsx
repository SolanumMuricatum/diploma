import React, { useState } from 'react';
import { toast } from 'react-toastify';
import '../styles/main.css';
import { useAuth } from '../auth/AuthProvider';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from '@fortawesome/free-solid-svg-icons'
import { Link } from 'react-router-dom';

export const Main = () => {
  return (
    <div className='main-container' >
        <div className='main-photoalbum-container'>
            <p>------ Фотоальбомы ------</p>
            <div>
              <Link to="/albums/created" className="main-page-button">
                Созданные
              </Link>
              <Link to="/albums/added" className="main-page-button">
                Добавленные
              </Link>
            </div>
        </div>
    </div>
  );
};
