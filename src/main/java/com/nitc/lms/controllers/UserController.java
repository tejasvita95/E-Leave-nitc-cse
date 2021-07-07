package com.nitc.lms.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nitc.lms.models.ConfirmationToken;
import com.nitc.lms.models.User;
import com.nitc.lms.repository.ConfirmationTokenRepository;
import com.nitc.lms.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test") //localhost:8080/api/test/employee
public class UserController 
{

	@Autowired
	private UserRepository userRepository;
	//private EmployeeServices employeeService;

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	// get all employees handler
	@GetMapping("/employees")
//	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	public List<User> getAllEmployee() 
	{
		return this.userRepository.findByDesignationNot();
	}

	@GetMapping("/professors")
	public List<String> getProfessors() 
	{
		return this.userRepository.findProfessors();
	}

	@GetMapping("/scholars")
	public List<User> getAllScholar(@RequestParam("role") String designation, int callerEmpId) 
	{
		if (designation.equals("HOD") || designation.equals("Program Coordinator")) 
		{
			return this.userRepository.findByDesignations();
		}
		else if (designation.equals("Faculty Advisor")) 
		{
			return this.userRepository.findByDesignation("M.Tech. Scholar");
		}
		else if (designation.equals("Professor")) 
		{
			return this.userRepository.findByRepotingPerson(callerEmpId);
		}
		
		return null;
	}

//	get employees by Id handler
	@GetMapping("/admin/employees/{id}") // admin/employees
	// @PreAuthorize("hasRole('ADMIN')")
	public User getEmployee(@PathVariable("id") int empId) 
	{
		return this.userRepository.findById(empId);
	}

	@GetMapping("/balance/{id}")
	public User getBalance(@PathVariable("id") int empId) 
	{
		return this.userRepository.findById(empId);
	}

	
	// activate user's account
	@Transactional
	@PutMapping("/employees/{empId}")
	public List<User> activateAccount(@PathVariable("empId") int empId) 
	{
		this.userRepository.activation(empId);
		return this.userRepository.findByDesignationNot();
	}

	// delete employee rest api
	@DeleteMapping("/employees/{id}")
	public List<User> deleteEmployee(@PathVariable int id) 
	{
		User user = userRepository.findById(id);
		// .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id
		// :" + id));
		ConfirmationToken ct = this.confirmationTokenRepository.findByEmpId(id);
		this.confirmationTokenRepository.delete(ct);
		this.userRepository.delete(user);
	
		return this.userRepository.findByDesignationNot();
	}
}
