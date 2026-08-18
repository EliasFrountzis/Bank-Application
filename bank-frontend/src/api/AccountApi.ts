import type { Account } from "../types/Account";

const API_URL = "http://localhost:4567";


export async function getUserAccounts(
    userId: number
): Promise<Account[]> {

    const response = await fetch(
        `${API_URL}/users/${userId}/accounts`
    );

    if (!response.ok) {
        throw new Error(
            "Failed to fetch accounts"
        );
    }

    return response.json();
}


export async function createAccount(
    userId: number,
    balance: number,
    cardLast4: string,
    name: string,
    type: string
): Promise<Account> {

    const response =
        await fetch(
            `${API_URL}/accounts`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    userId,
                    balance,
                    cardLast4,
                    name,
                    type
                })
            }
        );

    if (!response.ok) {

        const message =
            await response.text();

        throw new Error(
            message ||
            "Failed to create account"
        );
    }

    return response.json();
}




export async function closeAccount(
    accountId: number
): Promise<Account> {

    const response = await fetch(
        `${API_URL}/accounts/${accountId}/close`,
        {
            method: "POST"
        }
    );

    if (!response.ok) {

        const message =
            await response.text();

        throw new Error(
            message || "Failed to close account"
        );
    }

    return response.json();
}

export async function getAccountById(
    accountId: number
): Promise<Account> {

    const response = await fetch(
        `${API_URL}/accounts/${accountId}`
    );

    if (!response.ok) {
        throw new Error("Failed to fetch account");
    }

    return response.json();
}