package com.nitc.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nitc.lms.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	
	Optional<User> findByUsername(String username);
//	select * from users where designation != "administrator";
    
	@Query(value = "select * from USERS c where c.designation <> 'HOD' AND c.designation <> 'Admin'  AND c.is_enabled = TRUE ", nativeQuery = true)
	public List<User> findByDesignationNot(); 
	
	@Query(value = "select * from USERS c where (c.designation == 'M.Tech. Scholar' OR c.designation == 'P.hD. Scholar') AND c.is_enabled = TRUE ", nativeQuery = true)
	public List<User> findByDesignations();

	@Query(value = "select * from USERS c where c.designation == 'P.hD. Scholar' AND c.reportsTo =:callerEmpId AND c.is_enabled = TRUE ", nativeQuery = true)
	public List<User> findByRepotingPerson(int callerEmpId);
	
	@Query(value = "select * from USERS c where c.designation NOT IN :designation AND is_enabled = TRUE ", nativeQuery = true)
	public List<User> findByDesignationNotIn(List<String> designation);
	
	public User findById(int empId);
	
//	 @Query(value="SELECT u FROM USERS u WHERE u.email =:email",nativeQuery = true)
	 public User findByEmail(String email); 
	 
	public Boolean existsByUsername(String username);
	
	@Query(value = "SELECT account_status FROM USERS u WHERE u.username =:username", nativeQuery = true)
	public Boolean isActive(String username);
	
	@Query(value = "SELECT is_enabled FROM USERS u WHERE u.username =:username", nativeQuery = true)
	public Boolean isVerified(String username);
	
	public Boolean existsByEmail(String email);
	
	public List<User> findByDesignation(String designation);
	
	@Query(value = "SELECT * FROM users u WHERE u.designation ='Faculty Advisor'", nativeQuery = true)
	public User findFA();
	
	@Query(value = "SELECT * FROM users u WHERE u.designation ='M.Tech. Program Coordinator'", nativeQuery = true)
	public User findPCM(String designation);
	
	@Query(value = "SELECT * FROM users u WHERE u.designation ='Ph.D. Program Coordinator'", nativeQuery = true)
	public User findPCP(String designation);
	
	@Query(value = "SELECT role_id FROM USER_ROLES u WHERE u.emp_id =:id", nativeQuery = true)
	Integer isAdmin(int id);
	
	public List<User> findAll();
	
	@Query(value = "SELECT CONCAT_WS(' ', `first_name`, `last_name`) FROM users u WHERE u.designation ='Professor' AND u.account_status = TRUE", nativeQuery = true)
	public List<String> findProfessors();
	
	@Query(value = "SELECT * FROM USERS u WHERE CONCAT_WS(' ', `first_name`, `last_name`) like %:name%", nativeQuery = true)
	public User findByName(String name);
	
	@Modifying
	@Query(value = "UPDATE Users u set account_status = true where u.emp_id = :empId",nativeQuery = true)
	public void activation(int empId);

//	@Query(value = "SELECT * FROM USERS u WHERE  college_id = :collegeId", nativeQuery = true)
//	List<User> existsByCollegeId(String collegeId);
	
 // public Page<User> findAllByOrderByIdDesc(Pageable pageable);
    
}
