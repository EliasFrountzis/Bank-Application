import type { User } from "../types/User";

const API_URL = "http://localhost:4567";


export async function login(
    email: string,
    password: string
): Promise<User> {

    const response =
        await fetch(
            `${API_URL}/login`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                },

                body: JSON.stringify({
                    email,
                    password,
                }),
            }
        );


    if (!response.ok) {

        const message =
            await response.text();

        throw new Error(
            message || "Login failed"
        );
    }


    return response.json();
}


export async function register(
    name: string,
    email: string,
    password: string
): Promise<User> {

    const response =
        await fetch(
            `${API_URL}/users`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                },

                body: JSON.stringify({
                    name,
                    email,
                    password,
                }),
            }
        );


    if (!response.ok) {

        const message =
            await response.text();

        throw new Error(
            message || "Registration failed"
        );
    }


    return response.json();
}


export async function getUserByEmail(
    email: string
): Promise<User> {

    const response =
        await fetch(
            `${API_URL}/users/email?email=${encodeURIComponent(email)}`
        );


    if (!response.ok) {

        if (response.status === 404) {

            throw new Error(
                "Recipient not found"
            );
        }

        const message =
            await response.text();

        throw new Error(
            message ||
            "Failed to find recipient"
        );
    }


    return response.json();
}


export async function getUserByName(
    name: string
): Promise<User> {

    const response =
        await fetch(
            `${API_URL}/users/name?name=${encodeURIComponent(name)}`
        );


    if (!response.ok) {

        if (response.status === 404) {

            throw new Error(
                "Recipient not found"
            );
        }

        const message =
            await response.text();

        throw new Error(
            message ||
            "Failed to find recipient"
        );
    }


    return response.json();
}

export async function getUserById(
    userId: number
): Promise<User> {

    const response = await fetch(
        `${API_URL}/users/${userId}`
    );

    if (!response.ok) {
        throw new Error("Failed to fetch user");
    }

    return response.json();
}