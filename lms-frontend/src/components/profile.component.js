import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import AuthService from "../services/auth.service";
import userService from '../services/user.service';
export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: { },
      User:{}
    };
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser) 
          this.setState({ redirect: "/login" });
    userService.getBalance(currentUser.id).then( res => {
      this.setState({User:res.data,
        currentUser: currentUser,
         userReady: true
      });
      })
  }

  render() {
    console.log("sick",this.state.User.sickLeave)
    if (this.state.redirect) {
      return <Redirect to={this.state.redirect} />
    }

    const { currentUser } = this.state;

    return (
      <div className="container">
        {(this.state.userReady) ?
        <div>
         <header className="jumbotron" style={{color:'black'}}>
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
        <table>
          <tr>
            <th style={{paddingLeft:"30px" }}>Employee Id:</th>
            <td style={{paddingLeft:"15px" }}>{currentUser.id}</td>
            <th style={{paddingLeft:"30px" }}>Designation:</th>
            <td style={{paddingLeft:"15px" }}>{currentUser.designation}</td>
          </tr>
          
          <tr>
            <th style={{paddingLeft:"30px" }}>Name:</th>
            <td style={{paddingLeft:"15px" }}>{currentUser.firstName + " "+ currentUser.lastName}</td>
            <th style={{paddingLeft:"30px" }}>Email:</th>
            <td style={{paddingLeft:"15px" }}>{currentUser.email}</td>
          </tr>
          <tr>
            <th style={{paddingLeft:"30px" }}>Contact:</th>
            <td style={{paddingLeft:"15px" }}>{currentUser.mobileNo}</td>
            <th style={{paddingLeft:"30px" }}>Join Date:</th>
            <td style={{paddingLeft:"15px" }}>{currentUser.joinDate}</td>
          </tr>
          {/* <tr>
            <th style={{paddingLeft:"30px" }}>Authorities:</th>
               <td style={{paddingLeft:"15px" }}>
                {currentUser.roles &&
                currentUser.roles.map((role, index) => <li key={index}>{role}</li>)}
               </td>
          </tr> */}
        </table>
        
        <br></br>
        <br></br> 
        <br></br>
        <br></br>
      </div>: null}
      </div>
    );
  }
}
