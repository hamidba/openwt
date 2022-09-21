import React, {createContext, useContext, useMemo} from "react";
import {useLocalStorage} from "../helpers/local-storage";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {

    const [user, setUser] = useLocalStorage("user", null);

    const login = async (credential) => {
        return fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credential),
        }).then(response => response.json())
        .then(response => {
            if (response.token) {
                setUser(response);
            }
            return response;
        });
    };
    const logout = () => {
        return new Promise((res) => {
            setUser(null);
        });
    };

    const value = useMemo(
        () => ({
            user,
            login,
            logout
        }),
        [user]
    );
    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    return useContext(AuthContext);
};


