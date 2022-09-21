import React from 'react';
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import BoatList from "./components/boat/BoatList";
import BoatEdit from "./components/boat/BoatEdit";
import {ProtectedRoute} from "./routes/ProtectedRoute";
import Login from "./components/login/Login";

function App() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<Login/>}/>
                <Route
                    path="/boats"
                    exact={true}
                    element={
                        <ProtectedRoute>
                            <BoatList/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/boats/:id"
                    element={
                        <ProtectedRoute>
                            <BoatEdit/>
                        </ProtectedRoute>
                    }
                />

            </Routes>
        </Router>
    )
}

export default App;
