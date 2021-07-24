package com.nitc.lms.controllers;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nitc.lms.models.ConfirmationToken;
import com.nitc.lms.models.Request;
import com.nitc.lms.models.User;
import com.nitc.lms.payload.response.MessageResponse;
import com.nitc.lms.repository.RequestRepository;
import com.nitc.lms.repository.UserRepository;
import com.nitc.lms.services.EmailService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class RequestController {

	@Autowired
	private RequestRepository requestRepository;
		
	@Autowired
	private UserRepository userRepository;

	@Autowired
	public EmailService emailService;

	
	public User getUserById(int id) 
	{
		return  userRepository.findById(id);
	}

	
	
//	@GetMapping("/uname/{id}")
//	@PathVariable("id")	
	
	
	
	//get all requests
	@GetMapping("/leave-requests/{callerId}")
	@PreAuthorize("hasRole('ROLE_MOD') or hasRole('ROLE_ADMIN')")
	public List<Request> getAllRequest(@PathVariable("callerId") int callerId, @RequestParam("role") String designation) 
	{
		if (designation.equals("HOD")) 
		{
//			List<String> designations = new ArrayList<>();
//			designations.add("Program Coordinator M");
//			designations.add("Program Coordinator P");	
//			designations.add("Faculty Advisor");
//			designations.add("Non Teaching Staff");
//			designations.add("Professor");
//			designations.add("Ph.D. Scholar");
			return this.requestRepository.getReqForHOD();
		}
		else if (designation.equals("M.Tech. Program Coordinator")) 
		{
			return this.requestRepository.getReqForPCM();
		}
		else if (designation.equals("Ph.D. Program Coordinator")) 
		{
			return this.requestRepository.getReqForPCP();
		}
		else if (designation.equals("Faculty Advisor")) 
		{
			return this.requestRepository.getReqForFA();
		}
		else if (designation.equals("Professor")) 
		{
			System.out.println("Inside professor condition " + callerId);
			System.out.println("callerId " + callerId);
			System.out.println(this.requestRepository.getReqForProfById(callerId));
			
			return this.requestRepository.getReqForProfById(callerId);

		}
		return null;
	}
	
	
	
	
    //get Application by Id handler check by user himself/herself
	@GetMapping("/user/requests/{empId}")
	public List<Request> getRequestById(@PathVariable("empId") int empId) 
	{
		return this.requestRepository.findByEmpIdOrderByIdDesc(empId);
	}

	
	//this api called by admin/mod
	@GetMapping("/employee-requests/{id}")
	public List<Request> getAllRequestByEmplyoeeId(@PathVariable("id") int id,@RequestParam("role") String designation)
	{
		if (designation.equals("HOD"))
		{
			return this.requestRepository.findByEmpIdOrderByIdDesc(id);
		}
		return this.requestRepository.findByIdByDesignationNotOrderByDateDesc(id);
	}
	   	
	
	//this api called by admin/mod
//		@GetMapping("/unread-requests/{id}")
//		public int getUnreadRequestByEmplyoeeId(@PathVariable("id") int id,@RequestParam("role") String designation)
//		{
//			
//			List<Request> unread= new LinkedList<>();
//			if (designation.equals("HOD")) 
//			{
////				List<String> designations = new ArrayList<>();
////				designations.add("Program Coordinator");
////				designations.add("Faculty Advisor");
////				designations.add("Non Teaching Staff");
////				designations.add("Professor");
//
//				unread =  this.requestRepository.getReqForHOD();
//			}
//			else if (designation.equals("Ph.D. Program Coordinator")) 
//			{
//				unread =  this.requestRepository.getReqForPCP();
//			}
//			else if (designation.equals("M.Tech. Program Coordinator")) 
//			{
//				unread =  this.requestRepository.getReqForPCM();
//			}
//			else if (designation.equals("Faculty Advisor")) 
//			{
//				unread = this.requestRepository.getReqForFA();
//			}
//			else if (designation.equals("Professor")) 
//			{
////				unread =  this.requestRepository.getReqForProfById(callerId);
//			}
//			
//			int unreadCount=0;
//			for(Request r: unread)
//			{
//				if(r.getIsRead()==Boolean.TRUE)
//				{
//					unreadCount++;
//				}
//			}
//			
//			return unreadCount;
//		}
		
	
	
	@PutMapping("/user/cancel-leave/{empId}/{requestId}")
	public List<Request> cancelLeave(@PathVariable("empId") int empId,@PathVariable("requestId") int requestId, @RequestBody Request request)
	{
		System.out.println(request.toString());
		this.requestRepository.save(request);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate LocalStartDate = LocalDate.parse(request.getStartDate(), formatter);
		LocalDate LocalEndDate = LocalDate.parse(request.getEndDate(), formatter);
		Period difference = Period.between(LocalStartDate, LocalEndDate);
		int vaccationDays = difference.getDays() + 1;
		// return this.requestRepository.findByOrderByIdDesc();
		String requestType = request.getLeaveType();
		User user = this.userRepository.findById(empId);
		
		if(requestType.equals("Casual Leave")) 
	    {
        	user.setCasualLeave(vaccationDays);
        	this.userRepository.save(user);
        }
        else if(requestType.equals("Sick Leave")) 
        {
        	user.setSickLeave(vaccationDays);
        	this.userRepository.save(user);
        }
        else if(requestType.equals("Earned Leave")) 
        {
        	user.setEarnedLeave(vaccationDays);
        	this.userRepository.save(user);
        }
		
        return this.requestRepository.findByEmpIdOrderByIdDesc(empId);
	}
	
	//change request status by admin/mod (approve /decline)
	@PutMapping("/requests/{requestId}/{callerId}")
	public List<Request> changeRequestStatus(@PathVariable("requestId") int requestId,
			@PathVariable("callerId") int callerId, @RequestBody Request request,
			@RequestParam("role") String designation) 
	{
		System.out.println(request.toString());
		this.requestRepository.save(request);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate LocalStartDate = LocalDate.parse(request.getStartDate(), formatter);
        LocalDate LocalEndDate   = LocalDate.parse(request.getEndDate(), formatter);
//        Period difference = Period.between(LocalStartDate, LocalEndDate);   
//        int vacationDays=difference.getDays() + 1; 
		
		long vacationDays = ChronoUnit.DAYS.between(LocalStartDate, LocalEndDate) + 1;
        //return this.requestRepository.findByOrderByIdDesc();
        String requestType=request.getLeaveType();
        User user=this.userRepository.findById(request.getEmpId());
        
		if(request.getCurrentStatus().equals("Approved"))
		{

			if (requestType.equals("Casual Leave")) 
			{
				user.setCasualLeave( user.getCasualLeave()- vacationDays );
				this.userRepository.save(user);
			}
			else if (requestType.equals("Sick Leave")) 
			{
				user.setSickLeave( user.getSickLeave()- vacationDays);
				this.userRepository.save(user);
			}
			else if (requestType.equals("Earned Leave")) 
			{
				user.setEarnedLeave(user.getEarnedLeave()- vacationDays);
				this.userRepository.save(user);
			}
			
			sendStatusMail(user,"approved");
		}
		else if(request.getCurrentStatus().equals("Declined"))
		{
			sendStatusMail(user,"declined");
			
		}
		else if(request.getCurrentStatus().equals("Approved By FA"))
		{
			String progCordM = userRepository.findByDesignation("M.Tech. Program Coordinator").get(0).getEmail();
			sendRequestMail(progCordM,  user.getUsername()+"("+user.getDesignation()+")");
		}
		else if(request.getCurrentStatus().equals("Approved By Guide"))
		{
			String progCordP = userRepository.findByDesignation("Ph.D. Program Coordinator").get(0).getEmail();
			sendRequestMail(progCordP,  user.getUsername()+"("+user.getDesignation()+")");
		}
		else
		{
			String hod = userRepository.findByDesignation("HOD").get(0).getEmail();
			sendRequestMail(hod,  user.getUsername()+"("+user.getDesignation()+")");			
		}
		/**
		 * 
		 *   if (currentUser.designation === "Professor") {
            currRequest[0].currentStatus = "Approved By Guide";
        } else if (currentUser.designation === "Faculty Advisor") {
            currRequest[0].currentStatus = "Approved By FA";
        }
        else if (currentUser.designation === "") {
            currRequest[0].currentStatus = "Approved By M.Tech. Program Coordinator";
        }
        else if (currentUser.designation === "Ph.D. Program Coordinator") {
            currRequest[0].currentStatus = "Approved By Ph.D. Program Coordinator";
		 */
		if (designation.equals("HOD")) 
		{
//			List<String> designations = new ArrayList<>();
//			designations.add("Program Coordinator");
//			designations.add("Faculty Advisor");
//			designations.add("Non Teaching Staff");
//			designations.add("Professor");

			return this.requestRepository.getReqForHOD();
		}
		else if (designation.equals("Ph.D. Program Coordinator")) 
		{
			return this.requestRepository.getReqForPCP();
		}
		else if (designation.equals("M.Tech. Program Coordinator")) 
		{
			return this.requestRepository.getReqForPCM();
		}
		else if (designation.equals("Faculty Advisor")) 
		{
			return this.requestRepository.getReqForFA();
		}
		else if (designation.equals("Professor")) 
		{
			return this.requestRepository.getReqForProfById(callerId);
		}
		
		
		return null;
	}
		
	public void sendStatusMail(User u, String status) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(u.getEmail());
		mailMessage.setFrom("cse.nitc.2021@gmail.com");
		mailMessage.setSubject("Leave request final status");

		mailMessage.setText("Hi "+u.getFirstName()+" "+u.getLastName()+",\n your leave request has been " +status+".");

		System.out.println(mailMessage);
		emailService.sendEmail(mailMessage);
	}
	
	
   public void sendRequestMail(String to, String requestorName)
   {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setFrom("cse.nitc.2021@gmail.com");		
		mailMessage.setSubject("New Leave Request");
		
		mailMessage.setText("Hi, you have a new leave request from "+requestorName);
		
		System.out.println(mailMessage);
		emailService.sendEmail(mailMessage);
   }
   
	
	//apply new leave by user
	@PostMapping("/user/add-leave")
	public ResponseEntity<?> addRequest(@Valid @RequestBody Request request) 
	{
		System.out.println("check");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
		LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
//		Period difference = Period.between(startDate, endDate);
		long vacationDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

		if (request.getLeaveType().equals("")) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Please select type of leave"));
		}
		
		int empId = request.getEmpId();
		
		if (requestRepository.checkPendingRequest(empId) != null) 
		{
			return ResponseEntity.badRequest().body(
					new MessageResponse("Your last leave request is still pending, You can't make a new request"));
		}
		if (vacationDays <= 0) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid Period"));
		}
		
		User user = this.userRepository.findById(request.getEmpId());
		System.out.println("LeaveType " + request.getLeaveType());
		System.out.println(vacationDays > user.getSickLeave());
		
		if (request.getLeaveType().equals("Sick Leave") && vacationDays > user.getSickLeave()) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Don't have enough sick leave"));
		}
		else if (request.getLeaveType().equals("Casual Leave") && vacationDays > user.getCasualLeave()) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Don't have enough casual leave"));

		}
		else if (request.getLeaveType().equals("Earned Leave") && vacationDays > user.getEarnedLeave()) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Don't have enough earned leave"));
		}
		else 
		{
			List<Request> allApprovedRequests = this.requestRepository.getAllApprovedRequests(request.getEmpId());
			
			for(Request r: allApprovedRequests)
			{
				LocalDate rangeStartDate = LocalDate.parse(r.getStartDate(), formatter);
				LocalDate rangeEndDate = LocalDate.parse(r.getEndDate(), formatter);
				
				if(endDate.isBefore(rangeStartDate) || startDate.isAfter(rangeEndDate)) {}
				else
					return ResponseEntity.badRequest().body(new MessageResponse("Leave request already approved between given range!!!"));

			
			}
			
			if(request.getLeaveType().equals("Sick Leave") && request.getAttachment().trim().equals(""))
			{
				return ResponseEntity.badRequest().body(new MessageResponse("Invalid Link!!!"));		
			}
			
			
			
			Request res = this.requestRepository.save(request);
//		
//			if (request.getLeaveType().equals("Casual Leave"))
//			{
//				user.setCasualLeave(user.getCasualLeave() - vacationDays);
//			}
//			
//			if (request.getLeaveType().equals("Sick Leave")) 
//			{
//				user.setSickLeave(user.getSickLeave() - vacationDays);
//			}
//			
//			if (request.getLeaveType().equals("Earned Leave")) 
//			{
//				user.setEarnedLeave(user.getEarnedLeave() - vacationDays);
//			}

			System.out.println(request);
//			System.out.println(request.getDesignation());
//			System.out.println(request.getDesignation().equals("M.Tech. Program Coordinator"));
					
			if(request.getDesignation().equals("M.Tech. Scholar"))
			{
				request.setCurrentStatus("Waiting for FA Approval");
				User FA = userRepository.findFA();
				sendRequestMail(FA.getEmail(),  user.getUsername()+"("+user.getDesignation()+")");
				
			}
			else if(request.getDesignation().equals("Ph.D. Scholar"))
			{
				request.setCurrentStatus("Waiting for Guide Approval");
				User professor = getUserById(user.getReports_to());
				sendRequestMail(professor.getEmail(),  user.getUsername()+"("+user.getDesignation()+")");
			}
			else
			{
				User HOD = userRepository.findByDesignation("HOD").get(0);
				
//				List<User> hodList = userRepository.findByDesignation("HOD");
//				User HOD = hodList.get(0);
				
				sendRequestMail(HOD.getEmail(), user.getUsername() + "(" + user.getDesignation() + ")");
			}
//			
			User temp= getUserById(request.getEmpId());
			request.setEmpName(temp.getFirstName()+" "+temp.getLastName());
			request.setUsername(temp.getUsername());
			
			
			System.out.println(temp.getUsername());
			request.setDesignation(user.getDesignation());
			this.requestRepository.save(request);
			this.userRepository.save(user);
		
			return ResponseEntity.ok(new MessageResponse("Leave application submitted successfully!"));
		}
	}
}
