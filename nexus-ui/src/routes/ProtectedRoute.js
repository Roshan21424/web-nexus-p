import { Navigate, Outlet } from "react-router-dom";
import { useMyContext } from "../context/ContextProvider";

export function ProtectedRoute({ children, requireAdmin = false }) {
  const { jwtToken, loading, user } = useMyContext();

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gradient-to-br from-slate-50 via-purple-50 to-blue-50">
        <div className="w-16 h-16 border-4 border-purple-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  if (!jwtToken) {
    return <Navigate to="/login" replace />;
  }

  if (requireAdmin) {
    const storedUser = JSON.parse(localStorage.getItem("USER") || "{}");

    const hasAdminRole =
      storedUser.roles?.includes("ROLE_ADMIN") ||
      storedUser.roles?.includes("ADMIN");

    if (!hasAdminRole) {
      return <Navigate to="/" replace />;
    }
  }

  return children ? children : <Outlet />;
}

export function PublicRoute({ children }) {
  const { jwtToken, loading } = useMyContext();
  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gradient-to-br from-slate-50 via-purple-50 to-blue-50">
        <div className="w-16 h-16 border-4 border-purple-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  if (jwtToken) {
    const storedUser = JSON.parse(localStorage.getItem("USER") || "{}");
    const hasAdminRole =
      storedUser.roles?.includes("ROLE_ADMIN") ||
      storedUser.roles?.includes("ADMIN");

    if (hasAdminRole) {
      return <Navigate to="/admin" replace />;
    }
    return <Navigate to="/" replace />;
  }

  return children;
}
