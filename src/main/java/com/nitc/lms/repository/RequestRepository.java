package com.nitc.lms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nitc.lms.models.Request;
import com.nitc.lms.models.User;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface RequestRepository extends JpaRepository<Request,Integer> {
	
	@Query(value="select * from Requests a where a.emp_id =:id and a.final_status='Pending'", nativeQuery=true)
	public Request checkPendingRequest(int id);
	
	@Query(value="select role_id from Requests a where a.emp_id = :Id ", nativeQuery=true)
	public int findUserId(int Id);
	
	
	public List<Request> findAll();
	
	@Query(value="select * from Requests req where req.final_status <> 'Cancelled' and (req.current_status= 'Waiting for Approval' OR req.current_status='Approved By Ph.D. Program Coordinator' OR req.current_status='Approved By M.Tech. Program Coordinator') Order By request_date desc ", nativeQuery=true)
	public List<Request> getReqForHOD();
	
	@Query(value="select * from Requests req where req.final_status <> 'Cancelled' and (req.current_status = 'Approved By Guide') Order By request_date desc ", nativeQuery=true)
	public List<Request> getReqForPCP();
	
	@Query(value="select * from Requests req where req.final_status <> 'Cancelled' and (req.current_status = 'Approved By FA') Order By request_date desc ", nativeQuery=true)
	public List<Request> getReqForPCM();
	

	@Query(value="select * from Requests req where req.current_status = 'Waiting for FA Approval' AND req.final_status <> 'Cancelled' Order By request_date desc ", nativeQuery=true)
	public List<Request> getReqForFA();
	
	@Query(value="select * from Requests req where req.final_status = 'Approved' and req.emp_id = :empId Order By request_date desc ", nativeQuery=true)
	public List<Request> getAllApprovedRequests(int empId);
	
	@Query(value="SELECT * FROM requests r INNER JOIN users u ON r.emp_id = u.emp_id  where  r.designation = 'Ph.D. Scholar' AND u.reports_to =:callerId and r.current_status = 'Waiting for Guide Approval' AND r.final_status <> 'Cancelled' Order By request_date desc ", nativeQuery=true)
	//@Query(value="select * from Requests r", nativeQuery=true)
	public List<Request> getReqForProfById(int callerId);
	
	@Query(value="select * from Requests a where a.designation <> 'HOD' AND a.final_status <> 'Cancelled' Order By request_date desc ", nativeQuery=true)
	public List<Request> findByDesignationNotByOrderByIdDesc(String designation);
	
	@Query(value="select * from Requests a where a.emp_id =:id AND a.designation <> 'HOD' Order By request_date desc ", nativeQuery=true)
	public List<Request> findByIdByDesignationNotOrderByDateDesc(int id);
	
	public Request findById(int requestId);
	
	//@Query(value="select * from Requests a where a.emp_id = :empId ", nativeQuery=true)
	public List<Request> findByEmpIdOrderByIdDesc(int id);
	
	@Modifying
	@Query(value = "UPDATE Requests u set status = 'Approved' where u.id = :requestId",nativeQuery = true)
	public void approveLeaveRequest(int requestId);
	
	
   //public Page<Request> findAllByOrderByIdDesc(Pageable pageable);
}

