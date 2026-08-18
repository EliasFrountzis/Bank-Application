import { useState } from "react";
import { Account } from "../types/Account";
import { closeAccount } from "../api/AccountApi";

interface AccountSettingsProps {
    account: Account;
    onBack: () => void;
    onAccountUpdated: (account: Account) => void;
    onAccountClosed: () => void;
}

function AccountSettings({
    account,
    onBack,
    onAccountUpdated,
    onAccountClosed
}: AccountSettingsProps) {

    const [name, setName] = useState(account.name);
    const [type, setType] = useState(account.type);

    const [message, setMessage] = useState("");
    const [closing, setClosing] = useState(false);

    function handleSave() {

        const updatedAccount: Account = {
            ...account,
            name: name,
            type: type
        };

        onAccountUpdated(updatedAccount);

        setMessage("Account settings updated.");
    }

    async function handleCloseAccount() {

        const confirmed = window.confirm(
            "Are you sure you want to close this account? This action cannot be undone."
        );

        if (!confirmed) {
            return;
        }

        setClosing(true);
        setMessage("");

        try {

            await closeAccount(account.id);

            onAccountClosed();

        } catch (error) {

            setMessage(
                error instanceof Error
                    ? error.message
                    : "Failed to close account"
            );

            setClosing(false);
        }
    }

    return (
        <div className="auth-page">

            <div className="auth-card">

                <button
                    className="account-settings-back-button"
                    onClick={onBack}
                >
                    ← Back
                </button>

                <h1>
                    Account Settings
                </h1>

                <div className="auth-form">

                    {/* ACCOUNT NAME */}

                    <div className="form-group">

                        <label>
                            Account name
                        </label>

                        <input
                            type="text"
                            value={name}
                            onChange={(event) =>
                                setName(event.target.value)
                            }
                        />

                    </div>


                    {/* ACCOUNT TYPE */}

                    <div className="form-group">

                        <label>
                            Account type
                        </label>

                        <select
                            value={type}
                            onChange={(event) =>
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


                    {/* ACCOUNT INFORMATION */}

                    <div className="settings-account-details">

                        <div>

                            <span>
                                Card
                            </span>

                            <strong>
                                •••• {account.cardLast4}
                            </strong>

                        </div>


                        <div>

                            <span>
                                Balance
                            </span>

                            <strong>
                                €{account.balance.toFixed(2)}
                            </strong>

                        </div>

                    </div>


                    {/* SAVE */}

                    <button
                        className="auth-submit"
                        onClick={handleSave}
                    >
                        Save Changes
                    </button>


                    {/* MESSAGE */}

                    {message && (
                        <p className="success-message">
                            {message}
                        </p>
                    )}

                </div>


                {/* DANGER ZONE */}

                <div className="danger-zone">

                    <h2>
                        Danger Zone
                    </h2>

                    <p>
                        Closing this account is permanent and cannot be undone.
                    </p>

                    <button
                        className="button button-danger"
                        onClick={handleCloseAccount}
                        disabled={closing}
                    >
                        {closing
                            ? "Closing Account..."
                            : "Close Account"
                        }
                    </button>

                </div>

            </div>

        </div>
    );
}

export default AccountSettings;