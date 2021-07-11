import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import AuthService from "../services/auth.service";
import userService from '../services/user.service';
import { CircularProgressbar } from 'react-circular-progressbar';



export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: {},
      User: {}
    };
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser)
      this.setState({ redirect: "/login" });
    userService.getBalance(currentUser.id).then(res => {
      this.setState({
        User: res.data,
        currentUser: currentUser,
        userReady: true
      });
    })
  }

  render() {
    // console.log("sick", this.state.User.sickLeave)
    if (this.state.redirect) {
      return <Redirect to={this.state.redirect} />
    }

    const { currentUser } = this.state;

    return (
      <div className="container">
  {/* <div className="container" style={{ width: "600px" }}>
      <div style={{ margin: "20px" }}>
        <h3>bezkoder.com</h3>
        <h4>React upload Files</h4>
      </div>

      <UploadFiles />
    </div> */}

        {(this.state.userReady) ?
          <div>
            <div style={{ width: 250, height: 100, float: 'left', margin: '40px' }}>
              <header className="jumbotron" style={{ color: 'black' }}>
                <h3>Profile</h3>
                <h4>
                  <strong>Hi {currentUser.username} !</strong>
                </h4>
              </header>
              {/* <p>
          <strong>Token:</strong>{" "}
          {currentUser.accessToken.toString()}
          {currentUser.accessToken.substr(currentUser.accessToken.length - 20)}
        </p> */}
              <table className="styled-table">
                <tbody>
                  <tr>
                    {/* {currentUser.designation != "M.Tech. Scholar" && currentUser.designation != "Ph.D. Scholar" ?
                      <th>Employee Id:</th>
                      :
                      <th>Registration Number:</th>
                    }
                    <td>{currentUser.collegeId}</td> */}
                    <th>Designation:</th>
                    <td>{currentUser.designation}</td>

                  </tr>

                  <tr>
                    <th>Name:</th>
                    <td>{currentUser.firstName + " " + currentUser.lastName}</td>
                    <th>Email:</th>
                    <td>{currentUser.email}</td>
                  </tr>
                  <tr>
                    <th>Contact:</th>
                    <td>{currentUser.mobileNo}</td>
                    <th>Join Date:</th>
                    <td>{currentUser.joinDate}</td>
                  </tr>

                  {
                    currentUser.designation == "Ph.D. Scholar" ?
                      <tr>
                        <th>Guide Name:</th>
                        <td>{currentUser.guideName}</td>
                        <th></th>
                        <td></td>
                      </tr> : <tr></tr>
                  }


                  {/* <tr>
            <th style={{paddingLeft:"30px" }}>Authorities:</th>
               <td style={{paddingLeft:"15px" }}>
                {currentUser.roles &&
                currentUser.roles.map((role, index) => <li key={index}>{role}</li>)}
               </td>
          </tr> */}
                </tbody>
              </table>

            </div>

            {
              currentUser.designation !== "HOD" && currentUser.designation !== "Admin" ?
                <div>
                  {/* <div style={{ width: 150, height: 150, float: 'left', margin: '40px' }}>
                    <CircularProgressbar value={currentUser.sickLeave} maxValue={14} strokeWidth={5} text={currentUser.sickLeave + "/14"} />
                    <br></br>
                    <center><p style={{ fontSize: 22 }}><b>Sick Leave </b></p></center>
                  </div> */}

                  <div style={{ width: 150, height: 150, float: 'left', margin: '40px' }}>
                    <CircularProgressbar value={currentUser.casualLeave} strokeWidth={5} maxValue={7} text={currentUser.casualLeave + "/7"} />
                    <b><center><p style={{ fontSize: 22 }}>Casual Leave </p></center></b>
                  </div>

                  {
                    currentUser.designation != "M.Tech. Scholar" && currentUser.designation != "Ph.D. Scholar" ?
                      <div style={{ width: 150, height: 150, float: 'left', margin: '40px' }}>
                        <CircularProgressbar value={currentUser.earnedLeave} strokeWidth={5} maxValue={12} text={currentUser.earnedLeave + "/12"} />
                        <b> <center><p style={{ fontSize: 22 }}>Earned Leave </p></center></b>
                      </div>
                      : <div></div>
                  }
                </div>
                : <div></div>

            }

            <br></br>
            <br></br>
            <br></br>
            <br></br>
          </div> : null}

        <br></br>
        <br></br>
        <br></br>
        <br></br>
      </div>
    );
  }
}
