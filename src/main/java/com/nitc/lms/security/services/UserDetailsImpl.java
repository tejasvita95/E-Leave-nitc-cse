package com.nitc.lms.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nitc.lms.models.User;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private int empId;
	private String username;
	@JsonIgnore
	private String password;
	private String firstName;
	private String lastName;
	private String joinDate;
	private String email;
	private long mobileNo;
	private String designation;
	private double sickLeave;
	private double casualLeave;
	private double earnedLeave;
	private String guideName;
//	private String collegeId;
	private Collection<? extends GrantedAuthority> authorities;

	
	public UserDetailsImpl(int empId, String username, String password, String firstName, String lastName, String joinDate, String email, 
			long mobileNo, String designation,double sickLeave, double casualLeave, double earnedLeave,String guideName, Collection<? extends GrantedAuthority> authorities) {
		this.empId = empId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.joinDate = joinDate;
		this.email = email;
		this.mobileNo = mobileNo;
		this.designation = designation;
		this.authorities = authorities;
		this.setSickLeave(sickLeave);
		this.setCasualLeave(casualLeave);
		this.setEarnedLeave(earnedLeave);
		this.guideName=guideName;
//		this.collegeId = collegeId;
	}

	public static UserDetailsImpl build(User user, String guideName) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getEmpId(), 
				user.getUsername(), 
				user.getPassword(), 
				user.getFirstName(),
				user.getLastName(),
				user.getJoinDate(),
				user.getEmail(),
				user.getMobileNo(),
				user.getDesignation(),		
				user.getSickLeave(),
				user.getCasualLeave(),
				user.getEarnedLeave(),
				guideName,
//				user.getCollegeId(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
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

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int getId() {
		return empId;
	}
    
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(empId, user.empId);
	}

	public double getSickLeave() {
		return sickLeave;
	}

	public void setSickLeave(double sickLeave) {
		this.sickLeave = sickLeave;
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

	public String getGuideName() {
		return guideName;
	}

	public void setGuideName(String guideName) {
		this.guideName = guideName;
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
