import { createContext, useContext, useEffect, useMemo, useState } from "react";
import api from "../service/api";

const AppContext = createContext();

export default function ContextProvider({ children }) {
  const getAlreadyStoredToken = localStorage.getItem("JWT_TOKEN");
  const [jwtToken, setJwtToken] = useState(getAlreadyStoredToken);
  const [currentRoute, setCurrentRoute] = useState("ClassRoom");
  const [user, setUser] = useState(null);
  const [sessionId, setSessionId] = useState(null);
  const [sectionId,setSectionId] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      if (jwtToken) {
        try {
          setLoading(true);
          const { data } = await api.get("/auth/user");
          setUser(data);
          localStorage.setItem("USER", JSON.stringify(data));
        } catch (error) {
          localStorage.removeItem("JWT_TOKEN");
          localStorage.removeItem("USER");
          localStorage.removeItem("CSRF_TOKEN");
          setJwtToken(null);
          setUser(null);
        } finally {
          setLoading(false);
        }
      } else {
        setLoading(false);
      }
    };

    fetchUser();
  }, [jwtToken]);

  const contextValue = useMemo(
    () => ({
      jwtToken,
      user,
      sessionId,
      currentRoute,
      sectionId,
      loading,
      setJwtToken,
      setUser,
      setSessionId,
      setCurrentRoute,
    }),
    [jwtToken, user, sessionId, currentRoute, loading]
  );

  return (
    <AppContext.Provider value={contextValue}>{children}</AppContext.Provider>
  );
}

export const useMyContext = () => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error("useMyContext must be used within ContextProvider");
  }
  return context;
};
