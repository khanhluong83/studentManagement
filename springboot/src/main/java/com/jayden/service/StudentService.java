package com.jayden.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.jayden.dto.StudentDto;
import com.jayden.dto.StudentSearchDto;

public interface StudentService {

	long countAll(StudentSearchDto searchDto);
	
	List<StudentDto> searchAll(StudentSearchDto searchDto, Pageable pageable);
	
	Optional<StudentDto> getById(Long id);
	
	Optional<StudentDto> getByEmail(String email);
	
	StudentDto save(StudentDto courseDto);
	
	StudentDto update(Long id, StudentDto courseDto);
	
	void deleteById(Long id);
	
}
