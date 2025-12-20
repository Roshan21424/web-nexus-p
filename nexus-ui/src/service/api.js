import axios from "axios";

console.log("Server URL:", process.env.REACT_APP_SERVER_URL);

const api = axios.create({
  baseURL: `${process.env.REACT_APP_SERVER_URL}`,
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
  withCredentials: true,
});

let isFetchingCSRF = false;

api.interceptors.request.use(
  async (config) => {
    const JWTtoken = localStorage.getItem("JWT_TOKEN");

    if (JWTtoken) {
      config.headers.Authorization = `Bearer ${JWTtoken}`;
    }

    let csrfToken = localStorage.getItem("CSRF_TOKEN");

    if (csrfToken) {
      config.headers["X-XSRF-TOKEN"] = csrfToken;
    } else if (!isFetchingCSRF) {
      try {
        isFetchingCSRF = true;

        const response = await axios.get(
          `${process.env.REACT_APP_SERVER_URL}/auth/csrf`,
          { withCredentials: true }
        );

        csrfToken = response.data.token;
        localStorage.setItem("CSRF_TOKEN", csrfToken);

        config.headers["X-XSRF-TOKEN"] = csrfToken;
      } catch (error) {
        console.error("Error fetching CSRF token:", error);
      } finally {
        isFetchingCSRF = false;
      }
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => response,

  (error) => {
    const status = error.response?.status;

    switch (status) {
      case 401:
        console.error("Unauthorized - clearing session");

        localStorage.removeItem("JWT_TOKEN");
        localStorage.removeItem("USER");
        localStorage.removeItem("CSRF_TOKEN");

        if (!window.location.pathname.includes("/login")) {
          window.location.href = "/login";
        }
        break;

      case 403:
        console.error("Forbidden - insufficient permissions");
        break;

      case 419:
        console.error("CSRF token expired - refreshing");
        localStorage.removeItem("CSRF_TOKEN");
        break;

      case 500:
      case 502:
      case 503:
      case 504:
        console.error("Server error:", status);
        break;

      default:
        console.error("API Error:", error.message);
    }

    return Promise.reject(error);
  }
);

export default api;
