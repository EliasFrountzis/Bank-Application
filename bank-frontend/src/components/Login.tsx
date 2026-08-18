import { useState } from "react";
import { login } from "../api/UserApi";
import type { User } from "../types/User";
import logo from "../assets/SPlogo.png";

interface LoginProps {
    onLogin: (user: User) => void;
    onRegister: () => void;
}

function Login({
    onLogin,
    onRegister
}: LoginProps) {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (
        event: React.FormEvent
    ) => {

        event.preventDefault();

        setError("");

        try {

            const user =
                await login(
                    email,
                    password
                );

            onLogin(user);

        } catch (error) {

            setError(
                error instanceof Error
                    ? error.message
                    : "Login failed"
            );
        }
    };


    return (
        <div className="auth-page">

            <div className="auth-card">

                <div className="auth-logo">
                    <img
                        src={logo}
                        alt="SpBank logo"
                    />
                </div>


                <h1>
                    Welcome back
                </h1>

                <p className="auth-subtitle">
                    Sign in to your SpBank account.
                </p>


                <form
                    className="auth-form"
                    onSubmit={handleSubmit}
                >

                    <div className="form-group">

                        <label>
                            Email
                        </label>

                        <input
                            type="email"
                            value={email}
                            onChange={(event) =>
                                setEmail(event.target.value)
                            }
                            placeholder="you@example.com"
                            required
                        />

                    </div>


                    <div className="form-group">

                        <label>
                            Password
                        </label>

                        <input
                            type="password"
                            value={password}
                            onChange={(event) =>
                                setPassword(event.target.value)
                            }
                            placeholder="Your password"
                            required
                        />

                    </div>


                    {error && (
                        <p className="auth-error">
                            {error}
                        </p>
                    )}


                    <button
                        className="auth-submit"
                        type="submit"
                    >
                        Login
                    </button>

                </form>


                <div className="auth-footer">

                    <span>
                        Don't have an account?
                    </span>

                    <button
                        className="auth-link"
                        type="button"
                        onClick={onRegister}
                    >
                        Create an account
                    </button>

                </div>

            </div>

        </div>
    );
}

export default Login;