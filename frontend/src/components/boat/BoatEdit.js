import React, {useEffect, useState} from 'react';
import {Link, useNavigate, useParams} from "react-router-dom";
import AppNavbar from "../common/AppNavbar";
import {Button, Container, Form, FormGroup, Input, Label} from "reactstrap";
import {useAuth} from "../../services/auth-hook";

const BoatEdit = () => {

    const emptyState = {
        name: '',
        description: ''
    };

    const [boat, setBoat] = useState(emptyState);
    const navigate = useNavigate();
    const user = useAuth();
    const { id } = useParams();

    useEffect(() => {
        if (id !== 'new') {
            fetch(`/api/v1/boats/${id}`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + user.user.token,
                },
            })
                .then(response => response.json())
                .then(data => setBoat(data));
        }
    }, [id, setBoat]);

    const handleChange = (event) => {
        const { name, value } = event.target
        setBoat({ ...boat, [name]: value })
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        await fetch('/api/v1/boats' + (boat.id ? '/' + boat.id : ''), {
            method: (boat.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + user.user.token,
            },
            body: JSON.stringify(boat)
        });
        setBoat(emptyState);
        navigate('/boats');
    }


    return (<div>
            <AppNavbar/>
            <Container>
                <h2>{boat.id ? 'Edit Boat' : 'Add Boat'}</h2>
                <Form onSubmit={handleSubmit}>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={boat.name || ''}
                               onChange={handleChange} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="description">Description</Label>
                        <Input type="text" name="description" id="description" value={boat.description || ''}
                               onChange={handleChange} autoComplete="description"/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/boats">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    );
}

export default BoatEdit;
