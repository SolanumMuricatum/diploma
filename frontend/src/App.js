import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Header } from './components/Header';
import { Hello } from './pages/Hello';
import { Main } from './pages/Main';

function App() {
  return (
    <Router>
      <div className="App">
        <Header/>
        <Routes>
          <Route path="/" element={<Hello />}/>
          <Route path="/main" element={<Main />}/>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
