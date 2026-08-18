import { useState } from "react";
import { Account } from "../types/Account";
import logo from "../assets/SPlogo.png";

interface AccountOperationFormProps {
    account: Account;
    operation: "DEPOSIT" | "WITHDRAW";
    onBack: () => void;
    onSuccess: (account: Account) => void;
}

function AccountOperationForm({
    account,
    operation,
    onBack,
    onSuccess
}: AccountOperationFormProps) {

    const [amount, setAmount] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const isDeposit = operation === "DEPOSIT";

    async function handleSubmit(event: React.FormEvent) {
        event.preventDefault();

        setError("");

        const numericAmount = Number(amount);

        if (!amount || numericAmount <= 0) {
            setError("Please enter a valid amount.");
            return;
        }

        setLoading(true);

        try {
            const endpoint = isDeposit
                ? `/accounts/${account.id}/deposit`
                : `/accounts/${account.id}/withdraw`;

            const response = await fetch(
                `http://localhost:4567${endpoint}`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: numericAmount.toString()
                }
            );

            if (!response.ok) {
                const message = await response.text();

                throw new Error(
                    message || "Operation failed"
                );
            }

            const updatedAccount: Account =
                await response.json();

            onSuccess(updatedAccount);

        } catch (error) {

            console.error(error);

            setError(
                error instanceof Error
                    ? error.message
                    : "Operation failed"
            );

        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="page">

            <div className="auth-card">

                {/* Back button */}
                <button
                    className="button-back"
                    onClick={onBack}
                    disabled={loading}
                >
                    ← Back
                </button>

                <div className="auth-logo">
                    <img
                        src={logo}
                        alt="SpBank logo"
                    />
                </div>

                <h1>
                    {isDeposit
                        ? "Deposit Money"
                        : "Withdraw Money"}
                </h1>

                <p className="auth-subtitle">
                    {isDeposit
                        ? "Add money to your account."
                        : "Withdraw money from your account."}
                </p>

                <div className="account-details-hero">

                    <div className="account-details-type">
                        {account.type}
                    </div>

                    <h2>
                        {account.name}
                    </h2>

                    <p className="account-card-number">
                        Card •••• {account.cardLast4}
                    </p>

                    <div className="account-details-balance">
                        €{account.balance.toFixed(2)}
                    </div>

                </div>

                <form
                    className="auth-form"
                    onSubmit={handleSubmit}
                >

                    <div className="form-group">

                        <label>
                            Amount
                        </label>

                        <input
                            type="number"
                            min="10"
                            step="10"
                            placeholder="0.00"
                            value={amount}
                            onChange={(event) =>
                                setAmount(
                                    event.target.value
                                )
                            }
                            disabled={loading}
                        />

                    </div>

                    {error && (
                        <p className="auth-error">
                            {error}
                        </p>
                    )}

                    <div className="form-actions">

                        <button
                            type="button"
                            className="button-back"
                            onClick={onBack}
                            disabled={loading}
                        >
                            Cancel
                        </button>

                        <button
                            type="submit"
                            className="auth-submit"
                            disabled={loading}
                        >
                            {loading
                                ? "Processing..."
                                : isDeposit
                                    ? "Deposit"
                                    : "Withdraw"}
                        </button>

                    </div>

                </form>

            </div>

        </div>
    );
}

export default AccountOperationForm;