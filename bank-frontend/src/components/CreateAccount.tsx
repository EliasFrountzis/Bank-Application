import { useState } from "react";
import { createAccount } from "../api/AccountApi";
import logo from "../assets/SPlogo.png";

interface CreateAccountProps {
    userId: number;
    onAccountCreated: () => void;
    onBack: () => void;
}

function CreateAccount({
    userId,
    onAccountCreated,
    onBack
}: CreateAccountProps) {

    const [name, setName] = useState("");
    const [type, setType] = useState("CURRENT");
    const [balance, setBalance] = useState("");
    const [cardLast4, setCardLast4] = useState("");

    const [error, setError] = useState("");

    const handleSubmit = async (
        event: React.FormEvent
    ) => {

        event.preventDefault();

        setError("");

        try {

            await createAccount(
                userId,
                Number(balance),
                cardLast4,
                name,
                type
            );

            onAccountCreated();

        } catch (error) {

            setError(
                error instanceof Error
                    ? error.message
                    : "Failed to create account"
            );
        }
    };

    return (
        <div className="auth-page">

            <div className="auth-card">

                {/* LOGO */}

                <div className="auth-logo">
                <img
                    src={logo}
                    alt="SpBank logo"
                />
            </div>

                {/* TITLE */}

                <h1>
                    Create an account
                </h1>

                <p className="auth-subtitle">
                    Set up a new SpBank account.
                </p>


                {/* FORM */}

                <form
                    className="auth-form"
                    onSubmit={handleSubmit}
                >

                    <div className="form-group">

                        <label>
                            Account name
                        </label>

                        <input
                            type="text"
                            value={name}
                            onChange={event =>
                                setName(event.target.value)
                            }
                            placeholder="My Current Account"
                            required
                        />

                    </div>


                    <div className="form-group">

                        <label>
                            Account type
                        </label>

                        <select
                            value={type}
                            onChange={event =>
                                setType(event.target.value)
                            }
                        >

                            <option value="CURRENT">
                                Current
                            </option>

                            <option value="SAVINGS">
                                Savings
                            </option>

                        </select>

                    </div>


                    <div className="form-group">

                        <label>
                            Initial balance
                        </label>

                        <input
                            type="number"
                            min="0"
                            step="10"
                            value={balance}
                            onChange={event =>
                                setBalance(event.target.value)
                            }
                            placeholder="0"
                            required
                        />

                    </div>


                    <div className="form-group">

                        <label>
                            Card last 4 digits
                        </label>

                        <input
                            type="text"
                            maxLength={4}
                            pattern="[0-9]{4}"
                            value={cardLast4}
                            onChange={event =>
                                setCardLast4(event.target.value)
                            }
                            placeholder="1234"
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
                        Create Account
                    </button>

                </form>


                {/* BACK BUTTON */}

                <div className="auth-footer">

                    <button
                        className="auth-link"
                        type="button"
                        onClick={onBack}
                    >
                        ← Back to Dashboard
                    </button>

                </div>

            </div>

        </div>
    );
}

export default CreateAccount;