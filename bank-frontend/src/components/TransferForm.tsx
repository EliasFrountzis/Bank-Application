import { useState } from "react";

import { Account } from "../types/Account";
import { User } from "../types/User";
import logo from "../assets/SPlogo.png";

import {
    getUserByEmail,
    getUserByName
} from "../api/UserApi";

import {
    getUserAccounts
} from "../api/AccountApi";


interface TransferFormProps {

    account: Account;

    onBack: () => void;

    onConfirm: (
        recipient: User,
        recipientAccount: Account,
        amount: number,
        description: string
    ) => void;
}


function TransferForm({
    account,
    onBack,
    onConfirm
}: TransferFormProps) {

    const [search, setSearch] =
        useState("");

    const [recipient, setRecipient] =
        useState<User | null>(null);

    const [recipientAccounts, setRecipientAccounts] =
        useState<Account[]>([]);

    const [recipientAccount, setRecipientAccount] =
        useState<Account | null>(null);

    const [amount, setAmount] =
        useState("");

    const [description, setDescription] =
        useState("");

    const [error, setError] =
        useState("");

    const [loading, setLoading] =
        useState(false);


    async function handleFindRecipient() {

        setError("");

        setRecipient(null);
        setRecipientAccounts([]);
        setRecipientAccount(null);

        if (search.trim() === "") {

            setError(
                "Please enter a name or email."
            );

            return;
        }

        setLoading(true);

        try {

            let user: User;

            if (search.includes("@")) {

                user =
                    await getUserByEmail(
                        search.trim()
                    );

            } else {

                user =
                    await getUserByName(
                        search.trim()
                    );
            }


            const accounts =
                await getUserAccounts(
                    user.id
                );


            if (accounts.length === 0) {

                setError(
                    "This recipient does not have an account."
                );

                return;
            }


            const activeAccounts =
                accounts.filter(
                    account =>
                        account.status !== "CLOSED"
                );


            if (activeAccounts.length === 0) {

                setError(
                    "This recipient does not have an active account."
                );

                return;
            }


            setRecipient(user);

            setRecipientAccounts(
                activeAccounts
            );

            setRecipientAccount(null);

        } catch (error) {

            console.error(error);

            setError(
                error instanceof Error
                    ? error.message
                    : "Recipient not found."
            );

        } finally {

            setLoading(false);
        }
    }


    function handleConfirmTransfer() {

        setError("");

        if (
            !recipient ||
            !recipientAccount
        ) {

            setError(
                "Please select a recipient account."
            );

            return;
        }


        const transferAmount =
            Number(amount);


        if (
            !amount ||
            transferAmount <= 0
        ) {

            setError(
                "Please enter a valid amount."
            );

            return;
        }


        if (
            transferAmount >
            account.balance
        ) {

            setError(
                "Insufficient funds."
            );

            return;
        }


        if (
            description.trim() === ""
        ) {

            setError(
                "Please enter a reason for the transfer."
            );

            return;
        }


        onConfirm(
            recipient,
            recipientAccount,
            transferAmount,
            description.trim()
        );
    }


    return (

        <div className="auth-page">

            <div className="auth-card">

                {/* =========================
                    BACK
                ========================= */}

                <button
                    className="account-back-button"
                    onClick={onBack}
                >
                    ← Back
                </button>


                {/* =========================
                    LOGO / HEADER
                ========================= */}

                <div className="auth-logo">
                    <img
                        src={logo}
                        alt="SpBank logo"
                    />
                </div>

                <h1>
                    Transfer Money
                </h1>

                <p className="auth-subtitle">
                    Send money securely to another account.
                </p>


                {/* =========================
                    FROM ACCOUNT
                ========================= */}

                <div className="account-details-hero">

                    <div className="account-details-type">
                        From Account
                    </div>

                    <h1>
                        {account.name}
                    </h1>

                    <p className="account-card-number">
                        {account.type} · Card ••••{" "}
                        {account.cardLast4}
                    </p>

                    <div className="account-details-balance">
                        €{account.balance.toFixed(2)}
                    </div>

                </div>


                {/* =========================
                    TRANSFER FORM
                ========================= */}

                <form
                    className="auth-form"
                    onSubmit={(event) => {
                        event.preventDefault();
                        handleFindRecipient();
                    }}
                >

                    {/* RECIPIENT */}

                    <div className="form-group">

                        <label>
                            Recipient
                        </label>

                        <input
                            type="text"
                            value={search}
                            onChange={(event) => {

                                setSearch(
                                    event.target.value
                                );

                                setRecipient(null);
                                setRecipientAccounts([]);
                                setRecipientAccount(null);
                                setError("");

                            }}
                            placeholder="Name or email"
                            disabled={loading}
                        />

                    </div>


                    <button
                        type="submit"
                        className="auth-submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Searching..."
                            : "Find Recipient"}
                    </button>


                    {/* ERROR */}

                    {error && (

                        <p className="auth-error">
                            {error}
                        </p>

                    )}


                    {/* =========================
                        RECIPIENT FOUND
                    ========================= */}

                    {recipient && (

                        <>

                           <div className="transfer-recipient">

                                <div className="account-details-type">
                                    Recipient
                                </div>

                                <h2>
                                    {recipient.name}
                                </h2>

                                <p className="transfer-recipient-email">
                                    {recipient.email}
                                </p>

                            </div>

                            {/* =========================
                                RECIPIENT ACCOUNT
                            ========================= */}

                            <div className="form-group">

                                <label>
                                    Choose recipient account
                                </label>

                                {recipientAccounts.map(
                                    recipientAccountOption => (

                                        <div
                                            key={
                                                recipientAccountOption.id
                                            }

                                            onClick={() =>
                                                setRecipientAccount(
                                                    recipientAccountOption
                                                )
                                            }

                                            style={{
                                                padding: "15px",
                                                borderRadius: "12px",
                                                cursor: "pointer",
                                                border:
                                                    recipientAccount?.id ===
                                                    recipientAccountOption.id
                                                        ? "2px solid var(--gold)"
                                                        : "1px solid #3d4941",
                                                background:
                                                    recipientAccount?.id ===
                                                    recipientAccountOption.id
                                                        ? "rgba(201, 162, 39, 0.10)"
                                                        : "rgba(255,255,255,0.03)"
                                            }}
                                        >

                                            <strong>
                                                {
                                                    recipientAccountOption.name
                                                }
                                            </strong>

                                            <p
                                                className="transaction-meta"
                                            >
                                                {
                                                    recipientAccountOption.type
                                                }
                                            </p>

                                            <p
                                                className="transaction-meta"
                                            >
                                                Card ••••{" "}
                                                {
                                                    recipientAccountOption.cardLast4
                                                }
                                            </p>

                                        </div>

                                    )
                                )}

                            </div>


                            {/* =========================
                                AMOUNT
                            ========================= */}

                            <div className="form-group">

                                <label>
                                    Amount
                                </label>

                                <input
                                    type="number"
                                    value={amount}
                                    onChange={(event) =>
                                        setAmount(
                                            event.target.value
                                        )
                                    }
                                    placeholder="€0.00"
                                    min="0"
                                    step="0.01"
                                />

                            </div>


                            {/* =========================
                                REASON
                            ========================= */}

                            <div className="form-group">

                                <label>
                                    Reason
                                </label>

                                <input
                                    type="text"
                                    value={description}
                                    onChange={(event) =>
                                        setDescription(
                                            event.target.value
                                        )
                                    }
                                    placeholder="What's this transfer for?"
                                />

                            </div>


                            {/* =========================
                                BUTTONS
                            ========================= */}

                            <div className="form-actions">

                                <button
                                    type="button"
                                    className="button button-secondary"
                                    onClick={onBack}
                                >
                                    Cancel
                                </button>

                                <button
                                    type="button"
                                    className="button button-gold"
                                    onClick={
                                        handleConfirmTransfer
                                    }
                                >
                                    Continue
                                </button>

                            </div>

                        </>

                    )}

                </form>

            </div>

        </div>
    );
}


export default TransferForm;