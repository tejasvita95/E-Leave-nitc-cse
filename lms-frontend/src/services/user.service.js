import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/test/';

class UserService {
  getPublicContent() {
    return axios.get(API_URL + 'all');
  }
  getUserBoard() {
    return axios.get(API_URL + 'user', { headers: authHeader() });
  }
  getAdminBoard() {
    return axios.get(API_URL + 'admin', { headers: authHeader() });
  }
  getAllEmployee(Role){
    return axios.get(API_URL + 'employees?role='+Role,{ headers: authHeader() })
  }
  getProfessors(){
    return axios.get(API_URL + 'professors')
  }
 
  getEmployeeById(empId){
    return axios.get(API_URL + 'admin/employees/'+empId, { headers: authHeader() })
  }
  getAllRequest(callerId,designation){
   
    return axios.get(API_URL + 'leave-requests/'+callerId+'?role='+designation, { headers: authHeader() })
  }

  getAllRequestByEmplyoeeId(id,role){
    return axios.get(API_URL + 'employee-requests/'+id+'?role='+role, { headers: authHeader() })
  }

  getUnreadRequestByEmplyoeeId(id,role){
    return axios.get(API_URL + 'unread-requests/'+id+'?role='+role, { headers: authHeader() })
  }

  activateAccount(empId,role){
    return axios.put(API_URL + 'employees/' + empId+'?role='+role , { headers: authHeader() });
  }

  deleteEmployee(empId,role){
    return axios.delete(API_URL + 'employees/' + empId+'?role='+role);
  }

  applyLeave(leaveRequest){

    
    return axios.post(API_URL+ 'user/add-leave',leaveRequest);
  }
  
  getBalance(id){
    return axios.get(API_URL + 'balance/'+id, { headers: authHeader() })
  }
  changeRequestStatus(requestId,callerId,request,designation){
    return axios.put(API_URL + 'requests/' + requestId+ '/'+callerId +'?role='+ designation, request, {headers: authHeader() });
  }
  
  cancelLeave(empId,request,requestId){
    return axios.put(API_URL + 'user/cancel-leave/' + empId+'/'+requestId, request, {headers: authHeader() });
  }
  getRequestById(id){
     return axios.get(API_URL + 'user/requests/' + id , {headers: authHeader()});
  }
}

export default new UserService();
