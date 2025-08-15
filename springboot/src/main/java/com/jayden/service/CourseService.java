package com.jayden.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.jayden.dto.CourseDto;

public interface CourseService {

	long countAll();
	
	List<CourseDto> searchAll(Pageable pageable);
	
	Optional<CourseDto> getById(Long id);
	
	Optional<CourseDto> getByCode(String code);
	
	CourseDto save(CourseDto courseDto);
	
	CourseDto update(Long id, CourseDto courseDto);
	
	void deleteById(Long id);
	
}
