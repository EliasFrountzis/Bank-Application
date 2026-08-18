import type { Transaction } from "../types/Transaction";

const API_URL = "http://localhost:4567";

export async function getAccountTransactions(
    accountId: number
): Promise<Transaction[]> {

    const response = await fetch(
        `${API_URL}/accounts/${accountId}/transactions`
    );

    if (!response.ok) {
        throw new Error("Failed to fetch transactions");
    }

    return response.json();
}

