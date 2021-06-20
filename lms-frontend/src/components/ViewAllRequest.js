import React, { Component } from 'react';
import authService from '../services/auth.service';
import userService from '../services/user.service';
import { Redirect } from "react-router-dom";


class ViewAllRequest extends Component {

    constructor(props) {
        super(props)
        this.state = {
            Role:"",
            requests: [],
            redirect: null,
        }
        this.approveRequestHandler = this.approveRequestHandler.bind(this);
        this.declineRequestHandler = this.declineRequestHandler.bind(this);
        
    }

    viewRequestByEmplyoeeId(id){
        this.props.history.push(`/view-request/${id}`);
    }
    componentDidMount(){
        const currentUser = authService.getCurrentUser();
        console.log("current user ID",currentUser.id);
        console.log("current user role",currentUser.roles);
        console.log("current user designation",currentUser.designation);
        if (!currentUser) 
              this.setState({ 
                  redirect: "/login",
                }); 
        else if(currentUser && currentUser.roles.includes("ROLE_USER")){
            this.setState({ 
                redirect: "/profile",
              }); 
        }
        else{
            // console.log("currentUser",currentUser.id);
            // console.log("currentUser Clas",currentUser.className);
            userService.getAllRequest(currentUser.id,currentUser.designation).then((res) => {
                this.setState({
                    requests: res.data
                });
            });
        } 
    }
    approveRequestHandler(requestId){
           const currentUser = authService.getCurrentUser();
           console.log(requestId)
           let currRequest=this.state.requests.filter(res => res.id == requestId)
           if(currentUser.designation ==="Professor"){
               currRequest[0].currentStatus="Approved By Guide";
           }else if(currentUser.designation ==="Faculty Advisor"){
            currRequest[0].currentStatus="Approved By FA";
           }else if(currentUser.designation ==="Program Coordinator"){
            currRequest[0].currentStatus="Approved By Program Coordinator";
           }else if(currentUser.designation ==="HOD"){
             currRequest[0].currentStatus="Approved"
             currRequest[0].finalStatus="Approved"
           }
           
           console.log(currRequest);
           userService.changeRequestStatus(requestId,currentUser.id,currRequest[0],currentUser.designation).then( res => {
                this.setState({requests:res.data});
            });
    }
    declineRequestHandler(requestId){
        const currentUser = authService.getCurrentUser();
        console.log(requestId)
        let currRequest=this.state.requests.filter(res => res.id ==requestId)
        currRequest[0].finalStatus="Declined";
        currRequest[0].currentStatus="Declined";
        console.log(currRequest);
        userService.changeRequestStatus(requestId,currentUser.id,currRequest[0],currentUser.designation).then( res => {
             this.setState({requests:res.data});
         });
     }
  
    render() {
        if (this.state.redirect) {
            return <Redirect to={this.state.redirect}/>
          }
        return (
            <div >
                 <div children="row">
                    <table className="table table-striped table-bordered"style={{color:'white'}}>
                        <thead>
                            <tr>
                                <th>Request ID</th>
                                <th>Emp ID</th>
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
                                        <tr key={req.id}>
                                            <td>{req.id}</td>
                                            <td><a href="" onClick={()=> this.viewRequestByEmplyoeeId(req.empId)} style={{color:'black'}}>{req.empId}</a></td>
                                            <td>{req.designation}</td>
                                            <td>{req.requestDate}</td>
                                            <td>{req.leaveType}</td>
                                            <td>{req.startDate}</td>
                                            <td>{req.endDate}</td>
                                             {req.finalStatus == "Approved" ? 
                                                 <td style={{color:'#40D428'}}> Approved</td> :
                                                 (req.finalStatus == "Declined" ?
                                                 <td style={{color:'#FF0000'}}> Declined</td> :
                                                 <td style={{color:'#FFA500'}}> Pending</td>)
                                             }
                                            <td>
                                              <button disabled={req.finalStatus=="Approved" || req.finalStatus=="Declined"} style={{marginLeft: "5px",marginTop: "5px"}} 
                                                  onClick={ () => this.approveRequestHandler(req.id)} className="btn btn-info"> Approve </button>
                                              <button disabled={req.finalStatus=="Declined" || req.finalStatus=="Approved"} style={{marginLeft: "5px",marginTop: "5px"}}
                                                  onClick={ () => this.declineRequestHandler(req.id)} className="btn btn-danger"> Decline </button>
                                             </td>
                                        </tr>
                                )
                            }
                        </tbody>

                    </table>
                    {/* {this.state.totalpages.map((ele,index)=>{
                        return <button onClick={this.pageChange.bind(this)} id={index+1}>{index+1}</button>
                    })
                    } */}
                </div>
                <div>
                    <br></br><br></br>
                </div>
            </div>
        );
    }
}

export default ViewAllRequest;