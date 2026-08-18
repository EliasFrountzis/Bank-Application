import { Account } from "../types/Account";

interface AccountCardProps {
    account: Account;
    onClick: () => void;
}

function AccountCard({
    account,
    onClick
}: AccountCardProps) {

    return (
        <div
            className="account-card"
            onClick={onClick}
        >

            <div className="account-card-top">

                <div>
                    <span className="account-type">
                        {account.type}
                    </span>

                    <h2>
                        {account.name}
                    </h2>
                </div>

                <div className="card-chip">
                    ◈
                </div>

            </div>


            <div className="account-balance">

                <span>
                    Available balance
                </span>

                <strong>
                    €{account.balance.toFixed(2)}
                </strong>

            </div>


            <div className="account-card-bottom">

                <span>
                    •••• {account.cardLast4}
                </span>

                <span className="view-account">
                    View account →
                </span>

            </div>

        </div>
    );
}

export default AccountCard;