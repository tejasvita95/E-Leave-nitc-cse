import React, { Component } from 'react';
import userService from '../services/user.service';
import { MDBCard, MDBCardTitle, MDBCardBody, MDBBtn, tr, MDBCol, MDBContainer, MDBIcon } from 'mdbreact';

export default class ViewEmployeeById extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.match.params.id,
            employee: {}
        }
    }
    componentDidMount() {
        userService.getEmployeeById(this.state.id).then(res => {
            this.setState({ employee: res.data });
        })
    }
    render() {
        console.log("empdata ", this.state.employee)

        return (
            <div>
                <div className="card col-md-7 offset-md-3">
                    <h3 className="text-center"> Employee Details</h3>
                    <table className="styled-table">
                        <tbody>

                            {/* <MDBContainer style={{backgroundColor:'#33CAFF'}}> */}
                            <tr>
                                <td>Username</td>
                                <td>{this.state.employee.username}</td>

                            </tr>
                            <tr>
                                <td> Name</td>
                                <td>  {this.state.employee.firstName} {this.state.employee.lastName}</td>
                            </tr>

                            <tr>
                                <td> Date of Joining</td>
                                <td> {this.state.employee.joinDate}</td>
                            </tr>

                            <tr>

                                <td>  Email ID </td>
                                <td> {this.state.employee.email}</td>
                            </tr>

                            <tr>
                                <td>  Mobile Number</td>
                                <td>{this.state.employee.mobileNo}</td>
                            </tr>

                            <tr>
                                <td>  Designation </td>
                                <td> {this.state.employee.designation}</td>
                            </tr>

                            {/* <tr>
                                <td>  Remaining Sick Leave </td>
                                <td> {this.state.employee.sickLeave}</td>
                            </tr> */}

                            <tr>
                                <td>  Remaining Casual Leave </td>
                                <td> {this.state.employee.casualLeave}</td>
                            </tr>
                            {
                                this.state.employee.designation != "M.Tech. Scholar" && this.state.employee.designation != "Ph.D. Scholar" ?
                                    <tr>
                                        <td>  Remaining Earned Leave </td>
                                        <td> {this.state.employee.earnedLeave}</td>
                                    </tr>
                                    : <span></span>
                            }
                  </tbody>
                    </table>
                </div>
            </div>
        )
    }
}