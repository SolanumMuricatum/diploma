import './App.css';
import { BrowserRouter as Router, Routes, Route, BrowserRouter } from 'react-router-dom';
import { Header } from './components/Header';
import { Hello } from './pages/Hello';
import { CurrentAlbum } from './pages/album/CurrentAlbum';
import { Albums } from './pages/album/Albums';
import { Login } from './pages/Login';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { SupportPage } from './pages/SupportPage';
import PrivateRoute from './auth/PrivateRoute';
import { AuthProvider } from "./auth/AuthProvider";
import { Account } from './pages/Account';
import { Main } from './pages/Main';
import { CreatedAlbums } from './pages/album/CreatedAlbums';

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
              <Routes>
                <Route path="/" element={<Hello />} />
                <Route path="/login" element={<Login />} />
                <Route path="/account" element={<Account />} />
                <Route path="/main" element={<Main/>} />
                <Route path="/created/albums" element={<CreatedAlbums/>} />

                <Route
                  path="/album/:eventId"
                  element={
                    <PrivateRoute>
                      <CurrentAlbum />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/album/create"
                  element={
                    <PrivateRoute>
                      <Albums />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/album/edit/:eventId"
                  element={
                    <PrivateRoute>
                      <Albums />
                    </PrivateRoute>
                  }
                />

                <Route path="/help" element={<SupportPage />} />
              </Routes>
            </div>
          </AuthProvider>
        </Router>
      
      <ToastContainer position="top-right" autoClose={5000} />
    </>
  );
}

export default App;
