import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Header } from './components/Header';
import { Hello } from './pages/Hello';
import { Main } from './pages/Main';
import { EventPage } from './pages/EventPage';
import { Login } from './pages/Login';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { SupportPage } from './pages/SupportPage';
import PrivateRoute from './auth/PrivateRoute';
import { AuthProvider } from "./auth/AuthProvider";

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
      <AuthProvider>
        <Router>
          <div className="App">
            <Header />
            <Routes>
              <Route path="/" element={<Hello />} />
              <Route path="/login" element={<Login />} />

              <Route
                path="/main/:eventId"
                element={
                  <PrivateRoute>
                    <Main />
                  </PrivateRoute>
                }
              />
              <Route
                path="/event/create"
                element={
                  <PrivateRoute>
                    <EventPage />
                  </PrivateRoute>
                }
              />
              <Route
                path="/event/update/:eventId"
                element={
                  <PrivateRoute>
                    <EventPage />
                  </PrivateRoute>
                }
              />

              <Route path="/help" element={<SupportPage />} />
            </Routes>
          </div>
        </Router>
      </AuthProvider>
      <ToastContainer position="top-right" autoClose={5000} />
    </>
  );
}

export default App;
