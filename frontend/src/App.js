import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Header } from './components/Header';
import { Hello } from './pages/Hello';
import { Main } from './pages/Main';
import { EventPage } from './pages/EventPage';

function App() {
  return (
    <Router>
      <div className="App">
        <Header/>
        <Routes>
          <Route path="/" element={<Hello />}/>
          <Route path="/main/:eventId" element={<Main />}/>
          <Route path="/event/create" element={<EventPage />}/>
          <Route path="/event/update/:eventId" element={<EventPage />}/>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
