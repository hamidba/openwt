import AppNavbar from "../common/AppNavbar";
import {Button, Container, Form, FormGroup, Input, Label} from "reactstrap";
import {useNavigate} from "react-router-dom";
import React, {useState} from "react";
import {useAuth} from "../../services/auth-hook";


const Login = () => {

    const emptyState = {
        username: '',
        password: ''
    };

    const [credential, setCredential] = useState(emptyState);
    const navigate = useNavigate();
    const { login } = useAuth();


    const handleChange = (event) => {
        const { name, value } = event.target
        setCredential({ ...credential, [name]: value })
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        login(credential)
            .then(() => {
                setCredential(emptyState);
                navigate('/boats');
            });
    }



    return (<div>
            <AppNavbar/>
            <Container>
                <Form onSubmit={handleSubmit}>
                    <FormGroup>
                        <Label for="username">Username</Label>
                        <Input type="text" name="username" id="username" value={credential.username || ''}
                               onChange={handleChange} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="password">Password</Label>
                        <Input type="password" name="password" id="password" value={credential.password || ''}
                               onChange={handleChange} autoComplete="password"/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Login</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    );

};

export default Login;
