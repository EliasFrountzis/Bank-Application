import { Account } from "../types/Account";
import { User } from "../types/User";
import logo from "../assets/SPlogo.png";


interface TransferConfirmationProps {

    account: Account;

    recipient: User;

    recipientAccount: Account;

    amount: number;

    description: string;

    onBack: () => void;

    onSuccess: () => void;
}


function TransferConfirmation({
    account,
    recipient,
    recipientAccount,
    amount,
    description,
    onBack,
    onSuccess
}: TransferConfirmationProps) {


    async function handleConfirm() {

        try {

            const response =
                await fetch(
                    "http://localhost:4567/transfers",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            fromAccount: account.id,
                            toAccount: recipientAccount.id,
                            amount: amount,
                            description: description
                        })
                    }
                );


            if (!response.ok) {

                const message =
                    await response.text();

                throw new Error(
                    message ||
                    "Transfer failed"
                );
            }


            onSuccess();

        } catch (error) {

            console.error(error);

            alert(
                error instanceof Error
                    ? error.message
                    : "Transfer failed"
            );
        }
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


                <div className="auth-logo">
                    <img
                        src={logo}
                        alt="SpBank logo"
                    />
                </div>

                <h1>
                    Confirm Transfer
                </h1>

                <p className="auth-subtitle">
                    Please review the details before sending.
                </p>


                {/* =========================
                    FROM
                ========================= */}
<div className="transfer-recipient">

    <div className="account-details-type">
        From
    </div>

    <h2>
        {account.name}
    </h2>

    <p className="transfer-recipient-email">
        {account.type} · Card ••••{" "}
        {account.cardLast4}
    </p>

    <div className="transfer-recipient-divider">

        <span className="transaction-meta">
            Available balance
        </span>

        <strong className="transfer-recipient-balance">
            €{account.balance.toFixed(2)}
        </strong>

    </div>

</div>


                {/* =========================
                    TO
                ========================= */}
<div className="transfer-recipient">

    <div className="account-details-type">
        To
    </div>

    <h2>
        {recipient.name}
    </h2>

    <p className="transfer-recipient-email">
        {recipient.email}
    </p>

    <div className="transfer-recipient-divider">

        <strong>
            {recipientAccount.name}
        </strong>

        <p className="transaction-meta">
            {recipientAccount.type}
        </p>

        <p className="transaction-meta">
            Card ••••{" "}
            {recipientAccount.cardLast4}
        </p>

    </div>

</div>


                {/* =========================
                    TRANSFER DETAILS
                ========================= */}

                <div
                    style={{
                        marginTop: "25px"
                    }}
                >

                    <div
                        style={{
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                            paddingBottom: "12px"
                        }}
                    >

                        <span>
                            Amount
                        </span>

                        <strong
                            style={{
                                fontSize: "24px"
                            }}
                        >
                            €{amount.toFixed(2)}
                        </strong>

                    </div>


                    <div
                        style={{
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "flex-start",
                            gap: "20px"
                        }}
                    >

                        <span>
                            Reason
                        </span>

                        <strong
                            style={{
                                textAlign: "right"
                            }}
                        >
                            {description}
                        </strong>

                    </div>

                </div>


                {/* =========================
                    BUTTONS
                ========================= */}

                <div
                    className="form-actions"
                    style={{
                        marginTop: "30px"
                    }}
                >

                    <button
                        className="button button-secondary"
                        onClick={onBack}
                    >
                        Back
                    </button>


                    <button
                        className="button button-gold"
                        onClick={handleConfirm}
                    >
                        Confirm Transfer
                    </button>

                </div>

            </div>

        </div>
    );
}


export default TransferConfirmation;