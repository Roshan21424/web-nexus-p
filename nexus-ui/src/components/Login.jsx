import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { jwtDecode } from "jwt-decode";
import { Link, useNavigate } from "react-router-dom";
import InputField from "../components/utils/InputField";
import { useMyContext } from "../context/ContextProvider";
import api from "../service/api";

const Login = () => {
  const [loading, setLoading] = useState(false);
  const [step, setStep] = useState(1);
  const [JWTToken, setJWTToken] = useState("");
  const [error, setError] = useState("");
  const { setJwtToken } = useMyContext();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: { username: "", password: "", code: "" },
    mode: "onTouched",
  });

  const loginHandler = async (data) => {
    try {
      setLoading(true);
      setError("");

      const response = await api.post("/auth/login", data);

      if (response.status === 200 && response.data.jwtToken) {
        setJWTToken(response.data.jwtToken);
        const decodedToken = jwtDecode(response.data.jwtToken);
        const rolesFromResponse = response.data.roles || [];

        if (decodedToken.is2factorAuthEnabled) {
          setStep(2);
        } else {
          handleSuccessfulLogin(
            response.data.jwtToken,
            decodedToken,
            rolesFromResponse
          );
        }
      }
    } catch (error) {
      setError("Invalid credentials. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const onVerify2FactorAuthHandler = async (data) => {
    setLoading(true);
    setError("");

    try {
      const formData = new URLSearchParams();
      formData.append("code", data.code);
      formData.append("jwtToken", JWTToken);

      await api.post("/auth/verify-2factorAuth-login", formData, {
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      });

      const userResponse = await api.get("/auth/user");
      const rolesFromResponse = userResponse.data.roles || [];

      const decodedToken = jwtDecode(JWTToken);
      handleSuccessfulLogin(JWTToken, decodedToken, rolesFromResponse);
    } catch (error) {
      setError("Invalid 2FA code. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleSuccessfulLogin = (jwtToken, decodedToken, rolesFromResponse) => {
    const user = {
      username: decodedToken.sub,
      roles: rolesFromResponse,
    };

    localStorage.setItem("USER", JSON.stringify(user));
    localStorage.setItem("JWT_TOKEN", jwtToken);
    setJwtToken(jwtToken);

    if (
      rolesFromResponse.includes("ROLE_ADMIN") ||
      rolesFromResponse.includes("ADMIN")
    ) {
      window.location.href = "/admin";
    } else {
      window.location.href = "/";
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-50 via-purple-50 to-blue-50 p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <div className="w-16 h-16 mx-auto mb-4 rounded-2xl bg-gradient-to-br from-purple-600 to-blue-600 flex items-center justify-center">
            <span className="text-white text-2xl font-bold">N</span>
          </div>
          <h1 className="text-3xl font-bold text-slate-900 mb-2">
            {step === 1 ? "Welcome Back" : "Verify 2FA"}
          </h1>
          <p className="text-slate-500">
            {step === 1
              ? "Please enter your credentials"
              : "Enter your 2FA code"}
          </p>
        </div>

        <div className="bg-white rounded-3xl shadow-lg p-8 backdrop-blur-sm border border-slate-100">
          {step === 1 ? (
            <form onSubmit={handleSubmit(loginHandler)} className="space-y-4">
              <InputField
                required
                label="Username"
                id="username"
                type="text"
                message="*Username is required"
                placeholder="Enter your username"
                register={register}
                errors={errors}
              />
              <InputField
                required
                label="Password"
                id="password"
                type="password"
                message="*Password is required"
                placeholder="Enter your password"
                register={register}
                errors={errors}
              />

              {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
                  {error}
                </div>
              )}

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-gradient-to-r from-purple-600 to-blue-600 text-white font-semibold py-3 rounded-xl hover:shadow-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? "Logging in..." : "Log In"}
              </button>

              <div className="text-center space-y-2 text-sm">
                <Link
                  to="/forgot-password"
                  className="text-purple-600 hover:text-purple-700 block"
                >
                  Forgot Password?
                </Link>
                <p className="text-slate-500">
                  Don't have an account?{" "}
                  <Link
                    to="/signup"
                    className="text-purple-600 hover:text-purple-700 font-semibold"
                  >
                    Sign Up
                  </Link>
                </p>
              </div>
            </form>
          ) : (
            <form
              onSubmit={handleSubmit(onVerify2FactorAuthHandler)}
              className="space-y-4"
            >
              <InputField
                label="2FA Code"
                required
                id="code"
                type="text"
                message="*Code is required"
                placeholder="Enter your 2FA code"
                register={register}
                errors={errors}
              />

              {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
                  {error}
                </div>
              )}

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-gradient-to-r from-purple-600 to-blue-600 text-white font-semibold py-3 rounded-xl hover:shadow-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? "Verifying..." : "Verify 2FA"}
              </button>

              <button
                type="button"
                onClick={() => {
                  setStep(1);
                  setError("");
                }}
                className="w-full text-slate-600 hover:text-slate-800 text-sm font-medium"
              >
                ← Back to login
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default Login;