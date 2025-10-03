// src/components/PrivateRoute.js
import { Navigate } from "react-router-dom";
import { useAuth } from '../auth/AuthProvider';

function PrivateLazyRoute({ children }) {
  const { userId, loading, checkAuth } = useAuth();

  if (loading) return <div>Загрузка...</div>;

  return userId ? children : <Navigate to="/login" />;
}

export default PrivateLazyRoute;
