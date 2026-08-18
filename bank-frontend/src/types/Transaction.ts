export interface Transaction {
    id: number;
    accountId: number | null;
    type: string;
    fromAccount: number | null;
    toAccount: number | null;
    amount: number;
    description: string;
    timestamp: string;
}