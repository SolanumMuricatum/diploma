import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Header } from './components/Header';
import { Hello } from './pages/Hello';
import { Main } from './pages/Main';
import { CreateEvent } from './pages/CreateEvent';

function App() {
  return (
    <Router>
      <div className="App">
        <Header/>
        <Routes>
          <Route path="/" element={<Hello />}/>
          <Route path="/main/:eventId" element={<Main />}/>
          <Route path="/event/create" element={<CreateEvent />}/>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
