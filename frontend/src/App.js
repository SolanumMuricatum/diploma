import './App.css';
import { BrowserRouter as Router, Routes, Route, BrowserRouter } from 'react-router-dom';
import { Header } from './components/Header';
import { Hello } from './pages/Hello';
import { CurrentAlbum } from './pages/album/CurrentAlbum';
import { CreateUpdateAlbum } from './pages/album/CreateUpdateAlbum';
import { Login } from './pages/Login';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { SupportPage } from './pages/SupportPage';
import PrivateRoute from './auth/PrivateRoute';
import { AuthProvider } from "./auth/AuthProvider";
import { Account } from './pages/Account';
import { Main } from './pages/Main';
import { CreatedAlbums } from './pages/album/CreatedAlbums';
import { ScrollToTop } from './components/ScrollToTop';
import { ManagePhotos } from './pages/ManagePhotos';
import { AddedAlbums } from './pages/album/AddedAlbums';

function App() {
  return (
    <>
      {/* <Router>
        <div className="App">
          <Header/>
          <Routes>
            <Route path="/" element={<Hello />}/>
            <Route path="/login" element={<Login />}/>
            <Route path="/main/:eventId" element={<Main />}/>
            <Route path="/event/create" element={<EventPage />}/>
            <Route path="/event/update/:eventId" element={<EventPage />}/>
            <Route path="/help" element={<SupportPage />}/>
          </Routes>
        </div>
      </Router> */}
        <Router>
          <AuthProvider>
            <div className="App">
              <Header />
              <ScrollToTop /> 
              <Routes>
                <Route path="/" element={<Hello />} />
                <Route path="/login" element={<Login />} />
                <Route path="/account" element={<Account />} />
                <Route path="/main" element={<Main/>} />
                <Route path="/albums/created" element={<CreatedAlbums/>} />
                <Route path="/albums/added" element={<AddedAlbums/>} />
                <Route
                  path="/albums/create"
                  element={
                    <PrivateRoute>
                      <CreateUpdateAlbum />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/albums/manage/photos/:albumId"
                  element={
                    <PrivateRoute>
                      <ManagePhotos />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/albums/edit/:albumId"
                  element={
                    <PrivateRoute>
                      <CreateUpdateAlbum />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/albums/:parent/:albumId"
                  element={
                    <PrivateRoute>
                      <CurrentAlbum />
                    </PrivateRoute>
                  }
                />

                <Route path="/help" element={<SupportPage />} />
              </Routes>
            </div>
          </AuthProvider>
        </Router>
      
      <ToastContainer position="top-right" autoClose={1000} />
    </>
  );
}

export default App;
