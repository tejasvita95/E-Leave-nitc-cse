package com.nitc.lms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import com.nitc.lms.repository.UserRepository;

@Entity
@Table(name = "requests")
public class Request {


    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

//	@OneendDateMany(fetch = FetchType.LAZY)
//	@JoinTable(	name = "users", 
//				joinColumns = @JoinColumn(name = "emp_id"), 
//				inverseJoinColumns = @JoinColumn(name = "request_id"))
	private int empId;
	private String designation;
	private String leaveType;
	private String requestDate;
	private String startDate;
	private String endDate;
	private String finalStatus;
	private String currentStatus;
	private String empName;
	private String username;
	private String reason;
	private String attachment;
	
	
	
	public Request() {
		this.finalStatus = "Pending";
		this.currentStatus = "Waiting for Approval";
	}

	public Request(int empId, String designation, String leaveType, String requestDate, String startDate,
			String endDate, String empName, String username, String reason, String attachment) {
		super();
		this.empId = empId;
		this.designation = designation;
		this.leaveType = leaveType;
		this.requestDate = requestDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.finalStatus = "Pending";
		this.currentStatus = "Waiting for Approval";
		this.empName = empName;
		this.username = username;
		this.attachment = attachment;
		this.reason = reason;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String final_status) {
		this.finalStatus = final_status;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String current_status) {
		this.currentStatus = current_status;
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", empId=" + empId + ", designation=" + designation + ", leaveType=" + leaveType
				+ ", requestDate=" + requestDate + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", finalStatus=" + finalStatus + ", currentStatus=" + currentStatus +" username = "+username+ " attachment = "+attachment+" reason = "+reason+" ]";
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
