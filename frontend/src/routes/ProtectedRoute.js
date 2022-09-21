import {Navigate} from "react-router-dom";
import {useAuth} from "../services/auth-hook";


export const ProtectedRoute = ({ children }) => {
    const { user } = useAuth();

    if (!user) {

        // user is not authenticated
        return <Navigate to="/" replace/>;
    }
    return children;
};
