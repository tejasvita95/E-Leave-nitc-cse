package com.nitc.lms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nitc.lms.models.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    
    @Query(value="Select * from confirmation_token u where u.emp_id =:empId",nativeQuery=true)
    public ConfirmationToken findByEmpId(int empId);
}