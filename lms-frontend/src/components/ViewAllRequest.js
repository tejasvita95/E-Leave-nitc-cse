import React, { Component } from 'react';
import Input from "react-validation/build/input";
import authService from '../services/auth.service';
import userService from '../services/user.service';
import { Redirect } from "react-router-dom";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';



class ViewAllRequest extends Component {

    constructor(props) {
        super(props)
        this.state = {
            Role: "",
            requests: [],
            redirect: null,
            isScholarPresent: false,
            isStaffPresent: false,
            isHod: false,
            reason: ""
        }
        this.approveRequestHandler = this.approveRequestHandler.bind(this);
        this.declineRequestHandler = this.declineRequestHandler.bind(this);
        this.onChangeReason = this.onChangeReason.bind(this);

    }
    onChangeReason(op) {
        this.setState({
            reason: op.target.value
        });
    }


    viewRequestByEmplyoeeId(id) {
        this.props.history.push(`/view-request/${id}`);
    }
    componentDidMount() {
        const currentUser = authService.getCurrentUser();
        console.log("current user ID", currentUser.id);
        console.log("current user role", currentUser.roles);
        console.log("current user designation", currentUser.designation);

        this.setState({ isHod: currentUser.designation == "HOD" })

        if (!currentUser)
            this.setState({
                redirect: "/login",
            });
        else if (currentUser && currentUser.roles.includes("ROLE_USER")) {
            this.setState({
                redirect: "/profile",
            });
        }
        else {
            // console.log("currentUser",currentUser.id);
            // console.log("currentUser Clas",currentUser.className);
            userService.getAllRequest(currentUser.id, currentUser.designation).then((res) => {
                this.setState({
                    requests: res.data
                });
                this.setState({ isScholarPresent: this.state.requests.filter(req => req.designation == "M.Tech. Scholar" || req.designation == "Ph.D. Scholar").length != 0 });
                this.setState({ isStaffPresent: this.state.requests.filter(req => req.designation != "M.Tech. Scholar" && req.designation != "Ph.D. Scholar").length != 0 });

            });
            // userService.getAllEmployee(Role).then((res) => {
            //     this.setState({ employees: res.data });

            // });

        }
    }
    approveRequestHandler(requestId) {
        const currentUser = authService.getCurrentUser();
        console.log(requestId)
        let currRequest = this.state.requests.filter(res => res.id == requestId)
        if (currentUser.designation === "Professor") {
            currRequest[0].currentStatus = "Approved By Guide";
        } else if (currentUser.designation === "Faculty Advisor") {
            currRequest[0].currentStatus = "Approved By FA";
        }
        else if (currentUser.designation === "M.Tech. Program Coordinator") {
            currRequest[0].currentStatus = "Approved By M.Tech. Program Coordinator";
        }
        else if (currentUser.designation === "Ph.D. Program Coordinator") {
            currRequest[0].currentStatus = "Approved By Ph.D. Program Coordinator";
        }
        else if (currentUser.designation === "HOD") {
            currRequest[0].currentStatus = "Approved"
            currRequest[0].finalStatus = "Approved"
        }

        console.log(currRequest);
        userService.changeRequestStatus(requestId, currentUser.id, currRequest[0], currentUser.designation).then(res => {
            this.setState({ requests: res.data });
        });
    }
    declineRequestHandler(requestId) {
        const currentUser = authService.getCurrentUser();
        console.log(requestId)
        let currRequest = this.state.requests.filter(res => res.id == requestId)
        currRequest[0].finalStatus = "Declined";
        currRequest[0].currentStatus = "Declined";
        currRequest[0].reason = this.state.reason;
        console.log(currRequest);
        userService.changeRequestStatus(requestId, currentUser.id, currRequest[0], currentUser.designation).then(res => {
            this.setState({ requests: res.data });
        });
    }



    render() {
        if (this.state.redirect) {
            return <Redirect to={this.state.redirect} />
        }
        return (
            <div >
                <div children="row">
                    {/* <h2>{JSON.stringify(this.state.requests)}</h2>
                    <h1>{JSON.stringify(this.state.isScholarPresent)}</h1> */}

                    <Tabs>
                        <TabList>
                            <Tab>Scholars</Tab>
                            {
                                this.state.isHod == true &&
                                <Tab>Staff</Tab>
                            }
                        </TabList>

                        <TabPanel>

                            {this.state.isScholarPresent == true ?

                                <table className="table table-striped table-bordered" style={{ color: 'white' }}>
                                    <thead>
                                        <tr>
                                            <th>Username</th>
                                            <th>Student's Name</th>
                                            <th>Designation</th>
                                            <th>Request Date</th>
                                            <th>Leave Type</th>
                                            <th>From </th>
                                            <th>To </th>
                                            <th>Final Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            this.state.requests.map(req =>
                                            (
                                                req.designation == "M.Tech. Scholar" || req.designation == "Ph.D. Scholar" ?

                                                    <tr key={req.id}>
                                                        <td><u><a style={{ color: "#3FF" }} href="" onClick={() => this.viewRequestByEmplyoeeId(req.empId)} >{req.username}</a></u></td>
                                                        <td> {req.empName}</td>
                                                        <td>{req.designation=="Professor"?"Faculty":req.designation}</td>
                                                        <td>{req.requestDate}</td>
                                                        <td width="125px">{req.leaveType} &nbsp;
                                                            {
                                                                req.leaveType == "Sick Leave" &&
                                                                <a href={req.attachment} target="_blank" className="tooltip-test" data-toggle="tooltip"
                                                                    title="Click to view health certificates ">
                                                                    <img
                                                                        src={require('.././images/link.png')}
                                                                        alt="no data found"
                                                                        height="15"
                                                                        width="15"
                                                                    /></a>
                                                            }

                                                        </td>
                                                        <td width="120px">{req.startDate}</td>
                                                        <td width="120px">{req.endDate}</td>
                                                        {req.finalStatus == "Approved" ?
                                                            <td style={{ color: '#40D428' }}> Approved</td> :
                                                            (req.finalStatus == "Declined" ?
                                                                <td style={{ color: '#FF0000' }}> Declined</td> :
                                                                <td style={{ color: '#FFA500' }}> Pending</td>)
                                                        }
                                                        <td width="200px">
                                                            <button disabled={req.finalStatus == "Approved" || req.finalStatus == "Declined"} style={{ marginLeft: "5px", marginTop: "5px" }}
                                                                onClick={() => this.approveRequestHandler(req.id)} className="btn btn-info"> Approve </button>
                                                            <Popup backg trigger={<button disabled={req.finalStatus == "Declined" || req.finalStatus == "Approved"} style={{ marginLeft: "5px", marginTop: "5px" }}
                                                                className="btn btn-danger"> Decline </button>} position="right center">
                                                                <div>
                                                                    <textarea rows="5" cols="23" placeholder="specify reason" onChange={this.onChangeReason}></textarea>
                                                                    <button disabled={req.finalStatus == "Declined" || req.finalStatus == "Approved"} style={{ marginLeft: "5px", marginTop: "5px" }}
                                                                onClick={() => this.declineRequestHandler(req.id)} className="btn btn-danger"> Done </button>
                                                                </div>
                                                            </Popup>

                                                        </td>
                                                    </tr> : <span></span>

                                            )
                                            )
                                        }
                                    </tbody>
                                    {/* {this.state.totalpages.map((ele,index)=>{
                        return <button onClick={this.pageChange.bind(this)} id={index+1}>{index+1}</button>
                    })
                    } */}

                                </table>

                                :
                                <img
                                    src={require('.././images/no_data_found.png')}
                                    alt="no data found"
                                />

                            }
                        </TabPanel>

                        {
                            this.state.isHod == true &&
                            <TabPanel>


                                {this.state.isStaffPresent == true ?

                                    <table className="table table-striped table-bordered" style={{ color: 'white' }}>
                                        <thead>
                                            <tr>
                                                <th>Username</th>
                                                <th>Employee Name</th>
                                                <th>Designation</th>
                                                <th>Request Date</th>
                                                <th>Leave Type</th>
                                                <th>From </th>
                                                <th>To </th>
                                                <th>Final Status</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {
                                                this.state.requests.map(req =>
                                                (
                                                    req.designation != "M.Tech. Scholar" && req.designation != "Ph.D. Scholar" ?

                                                        <tr key={req.id}>
                                                            <td><u><a style={{ color: '#3FF' }} href="" onClick={() => this.viewRequestByEmplyoeeId(req.empId)}>{req.username}</a></u></td>
                                                            <td> {req.empName}</td>
                                                            <td>{req.designation=="Professor"?"Faculty":req.designation}</td>
                                                            <td>{req.requestDate}</td>
                                                            <td width="120px">{req.leaveType}
                                                                &nbsp;
                                                                {
                                                                    req.leaveType == "Sick Leave" &&
                                                                    <a href={req.attachment} target="_blank" className="tooltip-test" data-toggle="tooltip"
                                                                        title="Click to view health certificates ">
                                                                        <img
                                                                            src={require('.././images/link.png')}
                                                                            alt="no data found"
                                                                            height="15"
                                                                            width="15"
                                                                        /></a>
                                                                }

                                                            </td>
                                                            <td width="120px">{req.startDate}</td>
                                                            <td width="120px">{req.endDate}</td>
                                                            {req.finalStatus == "Approved" ?
                                                                <td style={{ color: '#40D428' }}> Approved</td> :
                                                                (req.finalStatus == "Declined" ?
                                                                    <td style={{ color: '#FF0000' }}> Declined</td> :
                                                                    <td style={{ color: '#FFA500' }}> Pending</td>)
                                                            }
                                                            <td width="200px">
                                                                <button disabled={req.finalStatus == "Approved" || req.finalStatus == "Declined"} style={{ marginLeft: "5px", marginTop: "5px" }}
                                                                    onClick={() => this.approveRequestHandler(req.id)} className="btn btn-info"> Approve </button>
                                                            <Popup backg trigger={<button disabled={req.finalStatus == "Declined" || req.finalStatus == "Approved"} style={{ marginLeft: "5px", marginTop: "5px" }}
                                                                className="btn btn-danger"> Decline </button>} position="right center">
                                                                <div>
                                                                    <textarea rows="5" cols="23" placeholder="specify reason" onChange={this.onChangeReason}></textarea>
                                                                    <button disabled={req.finalStatus == "Declined" || req.finalStatus == "Approved"} style={{ marginLeft: "5px", marginTop: "5px" }}
                                                                onClick={() => this.declineRequestHandler(req.id)} className="btn btn-danger"> Done </button>
                                                                </div>
                                                            </Popup>

                                                            </td>
                                                        </tr> : <span></span>

                                                )
                                                )
                                            }
                                        </tbody>
                                        {/* {this.state.totalpages.map((ele,index)=>{
       return <button onClick={this.pageChange.bind(this)} id={index+1}>{index+1}</button>
   })
   } */}

                                    </table>
                                    :
                                    <img
                                        src={require('.././images/no_data_found.png')}
                                        alt="no data found"
                                    />

                                }
                            </TabPanel>
                        }
                    </Tabs>
                </div>

                <div>
                    <br></br><br></br>
                </div>
            </div>
        );
    }
}

export default ViewAllRequest;