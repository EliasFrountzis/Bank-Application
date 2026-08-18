import { useEffect, useState } from "react";
import "./App.css";

import Navbar from "./components/Navbar";
import AccountCard from "./components/AccountCard";
import Login from "./components/Login";
import Register from "./components/Register";
import AccountDetails from "./components/AccountDetails";
import AccountSettings from "./components/AccountSettings";
import TransferForm from "./components/TransferForm";
import TransferConfirmation from "./components/TransferConfirmation";
import CreateAccount from "./components/CreateAccount";

import { Account } from "./types/Account";
import { User } from "./types/User";

import {
    getUserAccounts
} from "./api/AccountApi";


function App() {

    const [user, setUser] =
        useState<User | null>(null);

    const [accounts, setAccounts] =
        useState<Account[]>([]);

    const [selectedAccount, setSelectedAccount] =
        useState<Account | null>(null);

    const [showTransfer, setShowTransfer] =
        useState(false);

    const [showRegister, setShowRegister] =
        useState(false);

    const [showCreateAccount, setShowCreateAccount] =
        useState(false);

    const [showAccountSettings, setShowAccountSettings] =
        useState(false);

    const [
        transferConfirmation,
        setTransferConfirmation
    ] = useState<{

        recipient: User;

        recipientAccount: Account;

        amount: number;

        description: string;

    } | null>(null);


    // =========================
    // LOAD USER ACCOUNTS
    // =========================

    useEffect(() => {

        if (user === null) {
            return;
        }

        getUserAccounts(user.id)

            .then(data => {

                setAccounts(
                    data.filter(
                        account =>
                            account.status !== "CLOSED"
                    )
                );

            })

            .catch(error => {

                console.error(
                    "Failed to fetch accounts",
                    error
                );

            });

    }, [user]);


    if (user === null) {

        if (showRegister) {

            return (
                <Register

                    onRegister={(user) => {

                        setUser(user);
                        setShowRegister(false);

                    }}

                    onBackToLogin={() =>
                        setShowRegister(false)
                    }

                />
            );
        }

        return (
            <Login

                onLogin={(user) =>
                    setUser(user)
                }

                onRegister={() =>
                    setShowRegister(true)
                }

            />
        );
    }


   

    if (showCreateAccount) {

        return (
            <CreateAccount

                userId={
                    user.id
                }

                onBack={() =>
                    setShowCreateAccount(false)
                }

                onAccountCreated={() => {

                    setShowCreateAccount(false);

                    getUserAccounts(user.id)

                        .then(data => {

                            setAccounts(
                                data.filter(
                                    account =>
                                        account.status !==
                                        "CLOSED"
                                )
                            );

                        })

                        .catch(error =>
                            console.error(
                                "Failed to refresh accounts",
                                error
                            )
                        );
                }}

            />
        );
    }


  
    if (
        selectedAccount !== null &&
        showAccountSettings
    ) {

        return (
            <AccountSettings

                account={
                    selectedAccount
                }

                onBack={() =>
                    setShowAccountSettings(false)
                }

                onAccountUpdated={(updatedAccount) => {

                    setSelectedAccount(
                        updatedAccount
                    );

                    setAccounts(
                        currentAccounts =>
                            currentAccounts.map(
                                account =>
                                    account.id ===
                                    updatedAccount.id

                                        ? updatedAccount

                                        : account
                            )
                    );

                    setShowAccountSettings(false);

                }}

                onAccountClosed={() => {

                    setAccounts(
                        currentAccounts =>
                            currentAccounts.filter(
                                account =>
                                    account.id !==
                                    selectedAccount.id
                            )
                    );

                    setSelectedAccount(null);

                    setShowAccountSettings(false);

                }}

            />
        );
    }


    

    if (
        selectedAccount !== null &&
        transferConfirmation !== null
    ) {

        return (
            <TransferConfirmation

                account={
                    selectedAccount
                }

                recipient={
                    transferConfirmation.recipient
                }

                recipientAccount={
                    transferConfirmation.recipientAccount
                }

                amount={
                    transferConfirmation.amount
                }

                description={
                    transferConfirmation.description
                }

                onBack={() =>
                    setTransferConfirmation(null)
                }

                onSuccess={() => {

                    setTransferConfirmation(null);

                    setShowTransfer(false);

                    setSelectedAccount(null);

                    getUserAccounts(user.id)

                        .then(data => {

                            setAccounts(
                                data.filter(
                                    account =>
                                        account.status !==
                                        "CLOSED"
                                )
                            );

                        })

                        .catch(error =>
                            console.error(
                                "Failed to refresh accounts",
                                error
                            )
                        );
                }}

            />
        );
    }


    

    if (
        selectedAccount !== null &&
        showTransfer
    ) {

        return (
            <TransferForm

                account={
                    selectedAccount
                }

                onBack={() =>
                    setShowTransfer(false)
                }

                onConfirm={(
                    recipient,
                    recipientAccount,
                    amount,
                    description
                ) => {

                    setTransferConfirmation({

                        recipient,

                        recipientAccount,

                        amount,

                        description

                    });

                }}

            />
        );
    }


    
    if (
        selectedAccount !== null
    ) {

        return (
            <AccountDetails

                account={
                    selectedAccount
                }

                onBack={() =>
                    setSelectedAccount(null)
                }

                onTransfer={() =>
                    setShowTransfer(true)
                }

                onSettings={() =>
                    setShowAccountSettings(true)
                }

                onAccountUpdated={(updatedAccount) => {

                    setSelectedAccount(
                        updatedAccount
                    );

                    setAccounts(
                        currentAccounts =>
                            currentAccounts.map(
                                account =>
                                    account.id ===
                                    updatedAccount.id

                                        ? updatedAccount

                                        : account
                            )
                    );

                }}

            />
        );
    }


    return (
    <div>

        <Navbar
            onLogout={() => {
                setUser(null);
                setAccounts([]);
                setSelectedAccount(null);
                setShowTransfer(false);
                setTransferConfirmation(null);
                setShowCreateAccount(false);
                setShowAccountSettings(false);
            }}
        />

        <main className="dashboard">

            <div className="dashboard-header">

                <div>
                    <h1>
                        Good evening, {user.name} 👋
                    </h1>

                    <p>
                        Here's an overview of your accounts.
                    </p>
                </div>

                <button
                    className="create-account-button"
                    onClick={() =>
                        setShowCreateAccount(true)
                    }
                >
                    + Create Account
                </button>

            </div>


            <section className="accounts-section">

                <h2>
                    Your accounts
                </h2>

                <div className="account-grid">

                    {[...accounts]
                        .sort((a, b) => {

                            if (
                                a.type === "CURRENT" &&
                                b.type === "SAVINGS"
                            ) {
                                return -1;
                            }

                            if (
                                a.type === "SAVINGS" &&
                                b.type === "CURRENT"
                            ) {
                                return 1;
                            }

                            return 0;

                        })
                        .map(account => (

                            <AccountCard
                                key={account.id}
                                account={account}
                                onClick={() =>
                                    setSelectedAccount(account)
                                }
                            />

                        ))}

                </div>

            </section>

        </main>

    </div>
);
}


export default App;