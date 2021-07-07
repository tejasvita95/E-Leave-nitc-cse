import React, { Component } from 'react';
import authService from '../services/auth.service';
import userService from '../services/user.service';
import { Redirect } from "react-router-dom";


class MyRequests extends Component {

    constructor(props) {
        super(props)
        this.state = {
            requests: [],
            redirect: null,
        }
    }
    componentDidMount(){
        const currentUser = authService.getCurrentUser();
        let isAdmin =false
        if(currentUser){
            isAdmin =  currentUser.roles.includes("ROLE_ADMIN")
        }
      console.log("currentuser",currentUser.id)
        if (!currentUser) 
              this.setState({ 
                  redirect: "/login",
                }); 
        else if(isAdmin){
            this.setState({ 
                redirect: "/admin/requests",
              }); 
        }else{
            userService.getRequestById(currentUser.id).then((res) => {
                this.setState({requests: res.data});
            });
        }   
    }
    handleCancelLeave(requestId){
        const currentUser = authService.getCurrentUser();
        console.log(requestId)
        let currRequest=this.state.requests.filter(res => res.id ==requestId)
        // currRequest[0].currentStatus="Cancelled";
        currRequest[0].finalStatus="Cancelled";
        currRequest[0].currentStatus="Withdrawn By User";
        console.log(currRequest);
        userService.cancelLeave(currentUser.id,currRequest[0],requestId).then( res => {
             this.setState({requests:res.data});
         });
     }
    render() {
        if (this.state.redirect) {
            return <Redirect to={this.state.redirect} />
          }
        return (
            <div style={{color:'white'}}>
                 <div children="row"  style={{color:'white'}}>
                    
                        
                 { this.state.requests.length !=0 ?
                    <table className="table table-striped table-bordered"  style={{color:'white'}}>
                        <thead>
                            <tr>
                                <th>Request ID</th>
                                <th>Request Date</th>
                                <th>Leave Type</th>
                                <th>From </th>
                                <th>To </th>
                                <th>Final Status</th>
                                <th>Current Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.requests.map(req =>
                                        <tr key={req.id}>
                                            <td>{req.id}</td>
                                            <td>{req.requestDate}</td>
                                            <td>{req.leaveType}</td>
                                            <td>{req.startDate}</td>
                                            <td>{req.endDate}</td>
                                             {  
                                                  
                                                 req.finalStatus == "Approved" ? 
                                                 <td style={{color:'#40D428'}}> Approved</td> :
                                                 (req.finalStatus == "Declined" ?
                                                 <td style={{color:'#FF0000'}}> Declined</td> :

                                                 [req.finalStatus=="Pending" ?<td style={{color:'#FFA500'}}> Pending</td> :
                                                 <td style={{color:'#7E7E7E'}}> Cancelled</td>])
                                             }
                                             <td style={{color:'#FFA500'}}>{req.currentStatus}</td>
                                            <button disabled={req.finalStatus !=="Pending"} style={{marginLeft: "5px",marginTop: "5px"}} 
                                            onClick={ () => this.handleCancelLeave(req.id)} className="btn btn-info"> Withdraw </button>
                                        </tr>
                                )
                            }
                        </tbody>

                    </table>
                     :
                     <img
                     src={require('.././images/no_data_found.png')}
                     alt="no data found"
                   />
         
                 }
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

export default MyRequests;