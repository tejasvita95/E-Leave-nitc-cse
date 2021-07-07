package com.nitc.lms.payload.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private int id;
	private String username;
	private String firstName;
	private String lastName;
	private long mobileNo;
	private String email;
	private String joinDate;
	private String designation;
	private List<String> roles;
	private double casualLeave;
	private double earnedLeave;
	private double sickLeave;
	private String guideName;
	private String empName;
//	private String collegeId;

	public JwtResponse(String accessToken, int id, String username, String firstName, String lastName, long mobileNo,
			String email, String joinDate, String designation,double casualLeave,double earnedLeave, double sickLeave, String guideName, String empName, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNo = mobileNo;
		this.email = email;
		this.joinDate = joinDate;
		this.designation = designation;
		this.casualLeave = casualLeave;
		this.earnedLeave = earnedLeave;
		this.sickLeave = sickLeave;
		this.roles = roles;
		this.guideName = guideName;
		this.empName = empName;
//		this.collegeId = collegeId;
		
		System.out.println(this);
		System.out.println(empName);
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getCasualLeave() {
		return casualLeave;
	}

	public void setCasualLeave(double casualLeave) {
		this.casualLeave = casualLeave;
	}

	public double getEarnedLeave() {
		return earnedLeave;
	}

	public void setEarnedLeave(double earnedLeave) {
		this.earnedLeave = earnedLeave;
	}

	public double getSickLeave() {
		return sickLeave;
	}

	public void setSickLeave(double sickLeave) {
		this.sickLeave = sickLeave;
	}	
	
	@Override
	public String toString() {
		
		return "Leave [ casualLeave = "+casualLeave+", sickLeave = "+sickLeave+", earnedLeave = "+earnedLeave+"], guideName = "+guideName+".";
	}

	public String getGuideName() {
		return guideName;
	}

	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}
//
//	public String getCollegeId() {
//		return collegeId;
//	}
//
//	public void setCollegeId(String collegeId) {
//		this.collegeId = collegeId;
//	}

}
