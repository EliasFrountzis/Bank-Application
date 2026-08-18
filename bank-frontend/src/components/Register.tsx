import { useState } from "react";
import { register } from "../api/UserApi";
import type { User } from "../types/User";
import logo from "../assets/SPlogo.png";

interface RegisterProps {
    onRegister: (user: User) => void;
    onBackToLogin: () => void;
}

function Register({
    onRegister,
    onBackToLogin
}: RegisterProps) {

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);


    const handleSubmit = async (
        event: React.FormEvent
    ) => {

        event.preventDefault();

        setError("");
        setLoading(true);

        try {

            const user =
                await register(
                    name,
                    email,
                    password
                );

            onRegister(user);

        } catch (error) {

            setError(
                error instanceof Error
                    ? error.message
                    : "Registration failed"
            );

        } finally {

            setLoading(false);
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
                    Create your account
                </h1>

                <p className="auth-subtitle">
                    Join SpBank and start managing your money.
                </p>


                <form
                    className="auth-form"
                    onSubmit={handleSubmit}
                >

                    <div className="form-group">

                        <label>
                            Name
                        </label>

                        <input
                            type="text"
                            value={name}
                            onChange={(event) =>
                                setName(event.target.value)
                            }
                            placeholder="Your name"
                            required
                        />

                    </div>


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
                            placeholder="Create a password"
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
                        disabled={loading}
                    >
                        {loading
                            ? "Creating account..."
                            : "Create Account"}
                    </button>

                </form>


                <div className="auth-footer">

                    <span>
                        Already have an account?
                    </span>

                    <button
                        className="auth-link"
                        type="button"
                        onClick={onBackToLogin}
                    >
                        Back to Login
                    </button>

                </div>

            </div>

        </div>
    );
}

export default Register;