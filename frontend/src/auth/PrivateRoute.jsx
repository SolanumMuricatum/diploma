// src/components/PrivateRoute.js
import { Navigate } from "react-router-dom";
import { useAuth } from './AuthProvider';
import { useEffect } from "react";

function PrivateRoute({ children }) {
  const { userId, loading, checkAuth } = useAuth();

/*   useEffect(() => {
    checkAuth();
  }, []); */

  if (loading) return <div>Загрузка...</div>;

  return userId ? children : <Navigate to="/login" />;
}

export default PrivateRoute; 
