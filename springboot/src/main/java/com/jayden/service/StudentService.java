package com.jayden.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.jayden.dto.StudentDto;

public interface StudentService {

	long countAll();
	
	List<StudentDto> searchAll(Pageable pageable);
	
	Optional<StudentDto> getById(Long id);
	
	Optional<StudentDto> getByEmail(String email);
	
	StudentDto save(StudentDto courseDto);
	
	StudentDto update(Long id, StudentDto courseDto);
	
	void deleteById(Long id);
	
}
