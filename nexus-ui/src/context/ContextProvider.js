import { createContext, useContext, useEffect, useMemo, useState } from "react";
import api from "../service/api";

const AppContext = createContext();

export default function ContextProvider({ children }) {
  const getAlreadyStoredToken = localStorage.getItem("JWT_TOKEN")
    ? JSON.stringify(localStorage.getItem("JWT_TOKEN"))
    : null;
  const [jwtToken, setJwtToken] = useState(getAlreadyStoredToken);
  const [currentRoute, setCurrentRoute] = useState("ClassRoom");
  const [user, setUser] = useState(null);
  const [sessionId, setSessionId] = useState(null);

  useEffect(() => {
    const fetchUser = async () => {
      if (jwtToken) {
        const user = JSON.parse(localStorage.getItem("USER"));
        if (user?.username) {
          try {
            const { data } = await api.get("/auth/user");
            setUser(data);
          } catch (error) {
            console.log("Error fetching user", error);
          }
        }
      }
    };

    fetchUser();
  }, [jwtToken]);

  return (
    <AppContext.Provider
      value={useMemo(
        () => ({
          jwtToken,
          user,
          sessionId,
          currentRoute,
          setJwtToken,
          setUser,
          setSessionId,
          setCurrentRoute,
        }),
        [jwtToken, user, sessionId]
      )}
    >
      {children}
    </AppContext.Provider>
  );
}

export const useMyContext = () => {
  const context = useContext(AppContext);
  return context;
};
