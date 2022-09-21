import React, {useState} from 'react';
import {Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem, NavLink} from 'reactstrap';
import {Link, useNavigate} from 'react-router-dom';
import {useAuth} from "../../services/auth-hook";

const AppNavbar = () => {

    const [isOpen, setIsOpen] = useState(false);
    const auth = useAuth();
    const navigate = useNavigate();

    const logout = () => {
        auth.logout()
            .then(() => navigate('/'))
    }

    return (
        <Navbar color="dark" dark expand="md">
            <NavbarBrand tag={Link} to="/boats">Boat's App</NavbarBrand>
            <NavbarToggler onClick={() => { setIsOpen(!isOpen) }}/>
            <Collapse isOpen={isOpen} navbar>
                <Nav className="justify-content-end" style={{width: "100%"}} navbar>
                    {auth.user &&
                        <NavItem>
                            <NavLink onClick={logout}>Logout</NavLink>
                        </NavItem>
                    }
                </Nav>
            </Collapse>
        </Navbar>
    );
};

export default AppNavbar;
