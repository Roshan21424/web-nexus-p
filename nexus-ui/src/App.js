import {
  Route,
  BrowserRouter as Router,
  Routes,
  Outlet,
} from "react-router-dom";
import { ProtectedRoute, PublicRoute } from "./routes/ProtectedRoute";
import Login from "./components/Login";
import Signup from "./components/Signup";
import Admin from "./components/Admin";
import Home from "./components/Home";
import SideNav from "./components/SideNav";
import TopNav from "./components/TopNav";
import ClassRoom from "./components/ClassRoom";
import Repository from "./components/Repository";

export default function App() {
  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={
            <PublicRoute>
              <Login />
            </PublicRoute>
          }
        />
        <Route
          path="/signup"
          element={
            <PublicRoute>
              <Signup />
            </PublicRoute>
          }
        />

        <Route
          path="/admin"
          element={
            <ProtectedRoute requireAdmin={true}>
              <AdminLayout />
            </ProtectedRoute>
          }
        />

        <Route
          path="/"
          element={
            <ProtectedRoute requireAdmin={false}>
              <MainLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<ClassRoom />} />
          <Route path="repository" element={<Repository />} />
        </Route>
      </Routes>
    </Router>
  );
}

function AdminLayout() {
  return (
    <div className="min-h-screen w-screen bg-gray-50">
      <Admin />
    </div>
  );
}

function MainLayout() {
  return (
    <div className="flex h-screen w-screen bg-blue-50 text-white overflow-hidden">
      <div className="hidden md:flex md:flex-col w-[260px] shrink-0">
        <SideNav />
      </div>

      <div className="flex flex-col flex-1 overflow-hidden">
        <header className="flex-none p-6">
          <TopNav />
        </header>

        <main className="flex-1 overflow-y-auto px-6 py-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
