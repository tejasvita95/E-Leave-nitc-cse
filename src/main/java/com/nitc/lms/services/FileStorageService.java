package com.nitc.lms.services;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nitc.lms.models.FileDB;
import com.nitc.lms.repository.FileDBRepository;


@Service
public class FileStorageService {

  @Autowired
  private FileDBRepository fileDBRepository;

  public FileDB store(MultipartFile file, int reqId) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), reqId);

    return fileDBRepository.save(FileDB);
  }

  public FileDB getFile(int reqId) {
	  System.out.println(reqId);
    return fileDBRepository.findByReqId(reqId).get(0);
  }
  
  public Stream<FileDB> getAllFiles() {
    return fileDBRepository.findAll().stream();
  }
}
