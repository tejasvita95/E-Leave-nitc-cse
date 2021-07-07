package com.nitc.lms.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nitc.lms.models.ConfirmationToken;
import com.nitc.lms.models.ERole;
import com.nitc.lms.models.Role;
import com.nitc.lms.models.User;
import com.nitc.lms.payload.request.LoginRequest;
import com.nitc.lms.payload.request.SignupRequest;
import com.nitc.lms.payload.response.JwtResponse;
import com.nitc.lms.payload.response.MessageResponse;
import com.nitc.lms.repository.ConfirmationTokenRepository;
import com.nitc.lms.repository.RequestRepository;
import com.nitc.lms.repository.RoleRepository;
import com.nitc.lms.repository.UserRepository;
import com.nitc.lms.security.jwt.JwtUtils;
import com.nitc.lms.security.services.UserDetailsImpl;
import com.nitc.lms.services.EmailService;
import com.nitc.lms.repository.UserRepository;





@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	public AuthenticationManager authenticationManager;

	@Autowired
	public UserRepository userRepository;
    
	@Autowired
	public EmailService emailService;

	@Autowired
	public ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	public RoleRepository roleRepository;

	@Autowired
	public PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	public String getUserById(int id) 
	{
		User u = userRepository.findById(id);
		return u.getFirstName()+" "+u.getLastName();
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) 
	{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		//System.out.println("active ? " + userRepository.isActive(loginRequest.getUsername()));	
		
		if (!userRepository.isVerified(loginRequest.getUsername())) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is not verified!"));
		}
		 

		if (!userRepository.isActive(loginRequest.getUsername())) 
		{

			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: This account is not active. Please contact to administrator!"));
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
//       
//		System.out.println("designation"+userDetails.getDesignation());
//		System.out.println("casualLeave"+userDetails.getCasualLeave());
//		System.out.println("casualLeave"+userDetails.getSickLeave());
//		System.out.println("casualLeave"+userDetails.getEarnedLeave());
		//userDetails.getGuide()

		System.out.println(getUserById(userDetails.getId()));
		
		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
				userDetails.getFirstName(), userDetails.getLastName(), userDetails.getMobileNo(),
				userDetails.getEmail(), userDetails.getJoinDate(), userDetails.getDesignation(),
				userDetails.getCasualLeave(), userDetails.getEarnedLeave(), userDetails.getSickLeave(),
				userDetails.getGuideName(), getUserById(userDetails.getId()),  roles));

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) 
	{
		//validating user
		ResponseEntity invalidUserCheck= validateUser(signUpRequest);
				
		if(invalidUserCheck.getStatusCode() == HttpStatus.BAD_REQUEST)
		{
			return invalidUserCheck;
		}
		
		System.out.println("User Validation Successful");
		//user validation done
		
		// Create new user's account
		
		User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getJoinDate(),
				signUpRequest.getEmail(), signUpRequest.getMobileNo(), signUpRequest.getDesignation());

		System.out.println(user.toString());

		if (signUpRequest.getDesignation().equals("Ph.D. Scholar")) 
		{
			System.out.println("Guide:" + signUpRequest.getGuide());
		
			User professor = this.userRepository.findByName(signUpRequest.getGuide());
			System.out.println("Guide EMPID " + professor.getEmpId());
			user.setReports_to(professor.getEmpId());
		}
		else if (signUpRequest.getDesignation().equals("M.Tech. Scholar")) 
		{
			User FA = userRepository.findFA();
			
//			if (FA == null) 
//			{
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: FA does not exist !"));
//			}
//			user.setReports_to(FA.getEmpId());
		}

		
		// Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		String designation = signUpRequest.getDesignation();

//		if (strRoles == null) {
//			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//			roles.add(userRole);
//		} else {
//			strRoles.forEach(role -> {
		
		
		if (designation.equals("Professor") || designation.equals("M.Tech. Program Coordinator") || designation.equals("Ph.D. Program Coordinator")|| designation.equals("Faculty Advisor")) 
		{
			Role modRole = roleRepository.findByName(ERole.ROLE_MOD)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		
			roles.add(modRole);
		} 
		else 
		{
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			
			roles.add(userRole);
		}

		user.setRoles(roles);
		userRepository.save(user);
			
		ConfirmationToken confirmationToken = new ConfirmationToken(user);
		confirmationTokenRepository.save(confirmationToken);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Complete Registration!");
		mailMessage.setFrom("cse.nitc.2021@gmail.com");
		mailMessage.setText("To confirm your account, please click here : http://localhost:8080/api/auth/confirm-account?token="
						+ confirmationToken.getConfirmationToken());

		emailService.sendEmail(mailMessage);
		
		return ResponseEntity.ok(new MessageResponse("Activation link has been sent to your email."));
	}
	
	@GetMapping("/confirm-account")
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String confirmationToken){
		//System.out.println("Token= "+confirmationToken);
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
        	User user = userRepository.findByEmail(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            
        	SimpleMailMessage mailMessage = new SimpleMailMessage();
    		mailMessage.setTo(userRepository.findByDesignation("Admin").get(0).getEmail());
    		mailMessage.setSubject("New User Request");
    		mailMessage.setFrom("cse.nitc.2021@gmail.com");
    		mailMessage.setText("You have a new request for Account Approval from "+user.getDesignation()+".");

    		emailService.sendEmail(mailMessage);

            
            return ResponseEntity.status(HttpStatus.OK).body("Your Id has been verified successfully!");
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The link is invalid or broken!");

      
    }
	
	
	
	public ResponseEntity<?> validateUser(SignupRequest signUpRequest) {
		
		// Validating for Existing Users
		
		if (userRepository.existsByUsername(signUpRequest.getUsername())) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		
//		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//			return ResponseEntity
//					.badRequest()
//					.body(new MessageResponse("Error: Email is already in use!"));
//		}
		
		if (userRepository.existsByEmail(signUpRequest.getEmail())) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		
//		List<User> existsByCollegeId = userRepository.existsByCollegeId(signUpRequest.getCollegeId());
//		
//		System.out.println(existsByCollegeId.size() != 0);
//		if (existsByCollegeId.size()!=0)
//		{
//			return ResponseEntity.badRequest().body(new MessageResponse("Error: College Id is already in use!"));
//		}
		
		String currDes=signUpRequest.getDesignation();

		if(currDes==null)
		{
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Designation is mandatory!"));			
		}

		
		if (currDes.equals("Faculty Advisor") && userRepository.findFA() != null) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: FA already exists !"));
		}

		if (currDes.equals("M.Tech. Program Coordinator") && userRepository.findPCM(currDes) != null) 
		{
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Program Coordinator(M.Tech.) already exists !"));
		}

		if (currDes.equals("Ph.D. Program Coordinator") && userRepository.findPCP(currDes) != null) 
		{
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Program Coordinator(Ph.D.) already exists !"));
		}
		
		
		System.out.println(currDes.equals("Ph.D. Program Coordinator") && signUpRequest.getGuide()==null);
		
		
		if(currDes.equals("Ph.D. Scholar") && signUpRequest.getGuide()==null)
		{
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Choose your guide !"));			
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("Verified");
        
		//Validations completed.
	}

}
