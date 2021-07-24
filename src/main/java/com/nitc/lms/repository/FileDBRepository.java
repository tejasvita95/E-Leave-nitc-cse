package com.nitc.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nitc.lms.models.FileDB;


@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

	List<FileDB> findByReqId(int reqId);

}
