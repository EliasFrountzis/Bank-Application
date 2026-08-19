import { useEffect, useState } from "react";
import { Account } from "../types/Account";
import { Transaction } from "../types/Transaction";
import { getAccountTransactions } from "../api/TransactionApi";
import { getAccountById } from "../api/AccountApi";
import { getUserById } from "../api/UserApi";
import AccountOperationForm from "./AccountOperationForm";

interface AccountDetailsProps {
    account: Account;
    onBack: () => void;
    onTransfer: () => void;
    onAccountUpdated: (account: Account) => void;
    onSettings: () => void;
}

interface RelatedAccount {
    name: string;
    type: string;
    userName: string;
}

function AccountDetails({
    account,
    onBack,
    onTransfer,
    onAccountUpdated,
    onSettings
}: AccountDetailsProps) {

    const [transactions, setTransactions] =
        useState<Transaction[]>([]);

    const [showAllTransactions, setShowAllTransactions] =
        useState(false);

    const [operation, setOperation] =
        useState<"DEPOSIT" | "WITHDRAW" | null>(null);

    const [relatedAccounts, setRelatedAccounts] =
        useState<Record<number, RelatedAccount>>({});


    useEffect(() => {

        getAccountTransactions(account.id)
            .then(async data => {

                setTransactions(data);

                const accountIds = data
                    .filter(transaction =>
                        transaction.type === "TRANSFER"
                    )
                    .map(transaction => {

                        if (
                            transaction.fromAccount === account.id
                        ) {
                            return transaction.toAccount;
                        }

                        return transaction.fromAccount;
                    })
                    .filter(
                        (id): id is number =>
                            id !== null
                    );

                const uniqueAccountIds =
                    [...new Set(accountIds)];


                const accountResults =
                    await Promise.all(
                        uniqueAccountIds.map(
                            async id => {

                                try {

                                    const relatedAccount =
                                        await getAccountById(id);

                                    const user =
                                        await getUserById(
                                            relatedAccount.userId
                                        );

                                    return {
                                        id,
                                        name:
                                            relatedAccount.name,
                                        type:
                                            relatedAccount.type,
                                        userName:
                                            user.name
                                    };

                                } catch (error) {

                                    console.error(
                                        "Failed to fetch related account",
                                        error
                                    );

                                    return null;
                                }
                            }
                        )
                    );


                const accountMap:
                    Record<number, RelatedAccount> = {};


                accountResults.forEach(result => {

                    if (result !== null) {

                        accountMap[result.id] = {
                            name:
                                result.name,

                            type:
                                result.type,

                            userName:
                                result.userName
                        };
                    }
                });


                setRelatedAccounts(accountMap);

            })
            .catch(error => {

                console.error(error);

            });

    }, [account.id]);


    function isOutgoing(transaction: Transaction) {

        if (transaction.type === "WITHDRAWAL") {
            return true;
        }

        if (
            transaction.type === "TRANSFER" &&
            transaction.fromAccount === account.id
        ) {
            return true;
        }

        return false;
    }


    function getRelatedAccount(
        transaction: Transaction
    ): RelatedAccount | null {

        if (transaction.type !== "TRANSFER") {
            return null;
        }

        let relatedAccountId: number | null = null;


        if (
            transaction.fromAccount === account.id
        ) {

            relatedAccountId =
                transaction.toAccount;

        } else {

            relatedAccountId =
                transaction.fromAccount;
        }


        if (relatedAccountId === null) {
            return null;
        }


        return (
            relatedAccounts[relatedAccountId] ?? null
        );
    }


    function formatDate(timestamp: string) {

        const date = new Date(timestamp);

        return date.toLocaleString();
    }


    const recentTransactions =
        transactions
            .slice(-2)
            .reverse();


   
    // DEPOSIT / WITHDRAW
   

    if (operation !== null) {

        return (
            <AccountOperationForm
                account={account}
                operation={operation}

                onBack={() =>
                    setOperation(null)
                }

                onSuccess={(updatedAccount) => {

                    onAccountUpdated(
                        updatedAccount
                    );

                    setOperation(null);

                    getAccountTransactions(account.id)
                        .then(data => {
                            setTransactions(data);
                        })
                        .catch(error => {
                            console.error(error);
                        });

                }}
            />
        );
    }


    return (
        <div className="account-details-page">

            <div className="account-details-container">

                {/* HEADER */}

                <div className="account-details-header">

                    <button
                        className="button-back"
                        onClick={onBack}
                    >
                        ← Back
                    </button>


                    <button
                        className="logout-button"
                        onClick={onSettings}
                    >
                        ⚙ Account Settings
                    </button>

                </div>


                {}

                <div className="account-details-hero">

                    <div className="account-details-type">
                        {account.type}
                    </div>

                    <h1>
                        {account.name}
                    </h1>

                    <p className="account-card-number">
                        Card •••• {account.cardLast4}
                    </p>

                    <div className="account-details-balance">
                        €{account.balance.toFixed(2)}
                    </div>

                </div>


                {!showAllTransactions ? (

                    <div>

                        {/* RECENT TRANSACTIONS */}

                        <div
                            className="transactions-panel"
                            onClick={() =>
                                setShowAllTransactions(true)
                            }
                        >

                            <div className="transactions-header">

                                <h2>
                                    Recent Transactions
                                </h2>

                                <span>
                                    View all →
                                </span>

                            </div>


                            {recentTransactions.length === 0 ? (

                                <p className="no-transactions">
                                    No transactions yet.
                                </p>

                            ) : (

                                <div className="transaction-list">

                                    {recentTransactions.map(
                                        transaction => {

                                            const outgoing =
                                                isOutgoing(
                                                    transaction
                                                );

                                            const relatedAccount =
                                                getRelatedAccount(
                                                    transaction
                                                );


                                            return (
                                                <div
                                                    className="account-transaction-row"
                                                    key={transaction.id}
                                                >

                                                    <div>

                                                        <div className="transaction-description">
                                                            {
                                                                transaction.description
                                                            }
                                                        </div>


                                                        {relatedAccount && (

                                                            <div className="transaction-meta">

                                                                {outgoing
                                                                    ? "To: "
                                                                    : "From: "}

                                                                <strong>
                                                                    {
                                                                        relatedAccount.userName
                                                                    }
                                                                </strong>

                                                                {" • "}

                                                                {
                                                                    relatedAccount.name
                                                                }

                                                                {" • "}

                                                                {
                                                                    relatedAccount.type
                                                                }

                                                            </div>

                                                        )}

                                                    </div>


                                                    <strong
                                                        className={
                                                            outgoing
                                                                ? "transaction-negative"
                                                                : "transaction-positive"
                                                        }
                                                    >

                                                        {outgoing
                                                            ? "-"
                                                            : "+"}

                                                        €

                                                        {transaction.amount.toFixed(
                                                            2
                                                        )}

                                                    </strong>

                                                </div>
                                            );
                                        }
                                    )}

                                </div>

                            )}

                        </div>


                        {/* ACCOUNT ACTIONS */}

                        <div className="account-details-actions">

                            <button
                                className="button button-gold"
                                onClick={() =>
                                    setOperation("DEPOSIT")
                                }
                            >
                                Deposit Money
                            </button>


                            <button
                                className="button button-gold"
                                onClick={() =>
                                    setOperation("WITHDRAW")
                                }
                            >
                                Withdraw Money
                            </button>


                            <button
                                className="button button-gold"
                                onClick={onTransfer}
                            >
                                Transfer Money
                            </button>

                        </div>

                    </div>

                ) : (

                    /* FULL TRANSACTION HISTORY */

                    <div className="all-transactions">

                        <button
                            className="button-back"
                            onClick={() =>
                                setShowAllTransactions(false)
                            }
                        >
                            ← Recent transactions
                        </button>


                        <h2>
                            All Transactions
                        </h2>


                        {transactions.length === 0 ? (

                            <p className="no-transactions">
                                No transactions yet.
                            </p>

                        ) : (

                            <div className="transactions-panel">

                                {transactions
                                    .slice()
                                    .reverse()
                                    .map(transaction => {

                                        const outgoing =
                                            isOutgoing(
                                                transaction
                                            );

                                        const relatedAccount =
                                            getRelatedAccount(
                                                transaction
                                            );


                                        return (
                                            <div
                                                className="account-transaction-full"
                                                key={transaction.id}
                                            >

                                                <div className="transaction-full-top">

                                                    <strong>
                                                        {
                                                            transaction.description
                                                        }
                                                    </strong>


                                                    <strong
                                                        className={
                                                            outgoing
                                                                ? "transaction-negative"
                                                                : "transaction-positive"
                                                        }
                                                    >

                                                        {outgoing
                                                            ? "-"
                                                            : "+"}

                                                        €

                                                        {transaction.amount.toFixed(
                                                            2
                                                        )}

                                                    </strong>

                                                </div>


                                                {relatedAccount && (

                                                    <p>

                                                        {outgoing
                                                            ? "To: "
                                                            : "From: "}

                                                        <strong>
                                                            {
                                                                relatedAccount.userName
                                                            }
                                                        </strong>

                                                        {" • "}

                                                        {
                                                            relatedAccount.name
                                                        }

                                                        {" • "}

                                                        {
                                                            relatedAccount.type
                                                        }

                                                    </p>

                                                )}


                                                <p>
                                                    {
                                                        transaction.type
                                                    }
                                                </p>


                                                <p>
                                                    {
                                                        formatDate(
                                                            transaction.timestamp
                                                        )
                                                    }
                                                </p>

                                            </div>
                                        );
                                    })}

                            </div>

                        )}

                    </div>

                )}

            </div>

        </div>
    );
}

export default AccountDetails;
