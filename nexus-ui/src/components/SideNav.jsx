import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { LayoutDashboard, TrendingUp, Briefcase, Database, LogOut } from "lucide-react";
import { useMyContext } from "../context/ContextProvider";

export default function SideNav() {
    const location = useLocation();
    const navigate = useNavigate();
    const { setJwtToken, setUser, setCurrentRoute } = useMyContext();
    const [pathname, setPathname] = useState(location.pathname);

    const routes = [
        {
            name: "Dashboard",
            displayName: "ClassRoom",
            path: "/",
            icon: <LayoutDashboard size={22} color="#7F56D9" />,
            activeIcon: <LayoutDashboard size={22} color="white" />,
        },
        {
            name: "Feed",
            displayName: "Feed",
            path: "/feed",
            icon: <TrendingUp size={22} color="#2E90FA" />,
            activeIcon: <TrendingUp size={22} color="white" />,
        },
        {
            name: "Work Station",
            displayName: "Work Station",
            path: "/workstation",
            icon: <Briefcase size={22} color="#12B76A" />,
            activeIcon: <Briefcase size={22} color="white" />,
        },
        {
            name: "Database",
            displayName: "Repository",
            path: "/repository",
            icon: <Database size={22} color="#F63D68" />,
            activeIcon: <Database size={22} color="white" />,
        },
    ];

    useEffect(() => {
        setPathname(location.pathname);
        const currentRouteObj = routes.find(r => r.path === location.pathname);
        if (currentRouteObj) {
            setCurrentRoute(currentRouteObj.displayName);
        }
    }, [location.pathname, setCurrentRoute]);

    const handleNavigation = (path, displayName) => {
        setCurrentRoute(displayName);
        navigate(path);
    };

    const handleLogout = () => {
        localStorage.removeItem("JWT_TOKEN");
        localStorage.removeItem("USER");
        localStorage.removeItem("CSRF_TOKEN");
        
        setJwtToken(null);
        setUser(null);
        
        navigate("/login");
    };

    return (
        <aside className="h-screen w-64 bg-white border-r border-slate-200 flex flex-col shadow-lg">
            <div className="p-6 border-b border-slate-200">
                <button 
                    onClick={() => handleNavigation("/", "ClassRoom")} 
                    className="flex items-center gap-3 w-full text-left"
                >
                    <img
                        src="/logo.png"
                        alt="Nexus Logo"
                        className="w-12 h-12 rounded-xl object-cover shadow-md"
                    />
                    <div>
                        <h1 className="text-xl font-bold text-slate-900">Nexus</h1>
                        <p className="text-xs text-slate-500">Empowering Institutions</p>
                    </div>
                </button>
            </div>

            <nav className="flex-1 p-4 space-y-2">
                {routes.map((r) => {
                    const active = pathname === r.path;
                    return (
                        <button
                            key={r.name}
                            onClick={() => handleNavigation(r.path, r.displayName)}
                            className={`flex items-center gap-3 w-full px-4 py-3 rounded-xl font-semibold transition ${
                                active
                                    ? "bg-gradient-to-r from-purple-600 to-blue-600 text-white shadow-md"
                                    : "text-slate-700 hover:bg-slate-100"
                            }`}
                        >
                            {active ? r.activeIcon : r.icon}
                            {r.name}
                        </button>
                    );
                })}
            </nav>

            <div className="p-4 border-t border-slate-200">
                <button
                    onClick={handleLogout}
                    className="flex items-center gap-3 w-full px-4 py-3 rounded-xl font-semibold
                    hover:bg-red-50 text-slate-700 hover:text-red-600 transition"
                >
                    <LogOut size={22} color="#F04438" />
                    Logout
                </button>
            </div>
        </aside>
    );
}