import React, {useEffect, useState} from 'react';
import {Button, ButtonGroup, Container, Table} from 'reactstrap';
import AppNavbar from '../common/AppNavbar';
import {Link} from 'react-router-dom';
import {useAuth} from "../../services/auth-hook";


const BoatList = () => {

    const [boats, setBoats] = useState([]);
    const [loading, setLoading] = useState(false);
    const user = useAuth();

    console.log(user);

    useEffect(() => {
        setLoading(true);

        fetch('api/v1/boats', {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + user.user.token,
            },
        })
            .then(response => response.json())
            .then(data => {
                setBoats(data);
                setLoading(false);
            })
    }, []);

    if (loading) {
        return <p>Loading...</p>;
    }

    const remove = async (id) => {
        await fetch(`/api/v1/boats/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + user.user.token,
            }
        }).then(() => {
            let updatedBoats = [...boats].filter(i => i.id !== id);
            setBoats(updatedBoats);
        });
    }

    const boatList = boats.map(boat => {
        return <tr key={boat.id}>
                    <td style={{whiteSpace: 'nowrap'}}>{boat.name}</td>
                    <td>{boat.description}</td>
                    <td>
                        <ButtonGroup>
                            <Button size="sm" color="primary" tag={Link} to={"/boats/" + boat.id}>Edit</Button>
                            <Button size="sm" color="danger" onClick={() => remove(boat.id)}>Delete</Button>
                        </ButtonGroup>
                    </td>
                </tr>
    });

    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <div className="float-end">
                    <Button color="success" tag={Link} to="/boats/new">Create Boat</Button>
                </div>
                <h3>Boat List</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th width="20%">Name</th>
                        <th width="60%">Description</th>
                        <th width="10%">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                        {boatList}
                    </tbody>
                </Table>
            </Container>
        </div>
    );
}

export default BoatList;
