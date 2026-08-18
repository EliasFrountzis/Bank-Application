interface Transaction {
    id: number;
    description: string;
    amount: number;
}

interface TransactionListProps {
    transactions: Transaction[];
}

function TransactionList({ transactions }: TransactionListProps) {
    return (
        <div>
            <h2>Recent Transactions</h2>

            {transactions.map((transaction) => (
                <div key={transaction.id}>
                    <span>{transaction.description}</span>
                    <span>
                        €{transaction.amount.toFixed(2)}
                    </span>
                </div>
            ))}
        </div>
    );
}

export default TransactionList;