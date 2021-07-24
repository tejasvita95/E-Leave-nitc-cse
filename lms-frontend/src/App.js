import React, { Component } from "react";
import { Switch, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import backgroundImage from "./images/BG.jpeg";

import AuthService from "./services/auth.service";

import Login from "./components/login.component";
import Register from "./components/register.component";
import Home from "./components/HomePage.component";
import Profile from "./components/profile.component";
import BoardUser from "./components/board-user.component";
import BoardAdmin from "./components/board-admin.component";
import ViewAllEmployees from "./components/ViewAllEmployees";
import ApplyLeave from "./components/ApplyLeave";
import ViewAllRequest from "./components/ViewAllRequest";
import MyRequests from "./components/MyRequests";
import ViewEmployeeById from "./components/ViewEmployeeById";
import ViewRequestByEmployeeById from "./components/ViewRequestByEmployeeId";
import Badge from "react-bootstrap/Badge";

//import forgotPassword from "./components/forgotPassword";

class App extends Component {
  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      Role: "",
      currentUser: undefined,
      designation: undefined
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();

    if (user) {
      let userRole = "";
      if (user.roles.includes("ROLE_ADMIN")) userRole = "ADMIN";
      else if (user.roles.includes("ROLE_MOD")) userRole = "MOD";
      else userRole = "USER";
      this.setState({
        currentUser: user,
        // showModeratorBoard: user.roles.includes("ROLE_MODERATOR"),
        Role: userRole,
        designation: user.designation
      });
    }
  }

  logOut() {
    AuthService.logout();
  }

  render() {
    const { currentUser, Role } = this.state;
    return (
      <div style={{
        backgroundImage: `linear-gradient(to left bottom, #0f0978, #004ba8, #007ec1, #04aece, #8ddcdd)`,
        //backgroundImage: `url(${backgroundImage})`,
        color: 'black',
        backgroundPosition: 'center',
        backgroundSize: 'cover',
        backgroundRepeat: 'repeat',
        width: '100vw',
        minHeight: '100vh',
        height: 'auto'
      }}>

{/* <div style={{backgroundColor:'white',  width: '100%', textAlign:'right'}}>
          <img
            src={require('./images/logo.jpg')}
            alt="no data found"
           style={{ float:'left'}}
          />
          <h2 style={{  display:'inline' }}>  Leave Management System</h2>
        </div> */}
        {/* <br>
          </br> */}
        <nav className="navbar navbar-expand navbar-dark bg-dark">
          <Link to={"/home"} className="navbar-brand">
            LMS
          </Link>
          <div className="navbar-nav mr-auto">

            {/* {showModeratorBoard && (
              <li className="nav-item">
                <Link to={"/mod"} className="nav-link">
                  Moderator Board
                </Link>
              </li>
            )} */}

            {(this.state.designation !== "Admin" && (Role === "ADMIN" || Role === "MOD")) && (
              <li className="nav-item">
                <Link to={"/requests"} className="nav-link">
                  {/* <Badge variant="danger">
                    1
                  </Badge> */}
                  Leave Requests
                </Link>
              </li>
            )}

            {(this.state.designation === "Admin" && Role === "ADMIN") && (
              <li className="nav-item">
                <Link to={"/employees"} className="nav-link">
                  Manage Users
                </Link>

              </li>
            )}

{(this.state.designation === "HOD" && Role === "ADMIN") && (
              <li className="nav-item">
                <Link to={"/employees"} className="nav-link">
                  View Users
                </Link>

              </li>
            )}


            {/* {!isAdmin && currentUser && (
              <li className="nav-item">
                <Link to={"/user"} className="nav-link">
                  Dashboard
                </Link>
              </li>
            )} */}

            {Role !== "ADMIN" && currentUser && (
              <li className="nav-item">
                <Link to={"/leave-request"} className="nav-link">
                  Apply Leave
                </Link>
              </li>
            )}

            {Role !== "ADMIN" && currentUser && (
              <li className="nav-item">
                <Link to={"/my-requests"} className="nav-link">
                  My History
                </Link>
              </li>
            )}
          </div>

          {currentUser ? (
            <div className="navbar-nav ml-auto">

              <li className="nav-item">
                <Link to={"/profile"} className="nav-link">
                  {currentUser.firstName+" "+currentUser.lastName}
                </Link>
              </li>
              <li className="nav-item">
                <a href="/login" className="nav-link" onClick={this.logOut}>
                  LogOut
                </a>
              </li>
            </div>
          ) : (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/login"} className="nav-link">
                  <b>Login</b>
                </Link>
              </li>

              <li className="nav-item">
                <Link to={"/register"} className="nav-link">
                  <b> Sign Up</b>
                </Link>
              </li>
            </div>
          )}
        </nav>

        <div className="container mt-3">
          <Switch>
          <Route exact path="/login" component={Login} />
          <Route exact path={["/", "/home"]} component={Home} />
            {/* <Route exact path="/login" component={Login} /> */}
            <Route exact path="/register" component={Register} />
            <Route exact path="/profile" component={Profile} />
            <Route path="/user" component={BoardUser} />
            <Route path="/leave-request" component={ApplyLeave} />
            <Route path="/employees" component={ViewAllEmployees} />
            {/* <Route path="/mod" component={BoardModerator} /> */}
            <Route path="/requests" component={ViewAllRequest} />
            <Route path="/my-requests" component={MyRequests} />
            <Route path="/view-emplyee/:id" component={ViewEmployeeById} />
            <Route path="/view-request/:id" component={ViewRequestByEmployeeById} />
          </Switch>
        </div>
        <br />
        <br />
        <br />
      </div>
    );
  }
}

export default App;
