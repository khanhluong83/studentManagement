package com.jayden.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jayden.dto.CourseDto;
import com.jayden.dto.ResponseDto;
import com.jayden.dto.SearchResultDto;
import com.jayden.service.CourseService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/course")
@CrossOrigin(origins = "*")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@GetMapping
	public ResponseEntity<SearchResultDto<CourseDto>> getAllCourses(@PageableDefault(size = 10) Pageable pageable) {
		long courseCount = courseService.countAll();
		List<CourseDto> courseList = courseService.searchAll(pageable);
		var searchResultDto = new SearchResultDto<CourseDto>(courseCount, courseList);
		
		return ResponseEntity.ok(searchResultDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CourseDto> getCourseById(@PathVariable(name = "id") Long id) {
		Optional<CourseDto> course = courseService.getById(id);
		
		return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<ResponseDto<CourseDto>> createCourse(@RequestBody CourseDto courseDto) {
		
		if (courseDto.getCode() == null || StringUtils.isBlank(courseDto.getCode())) {
			throw new IllegalArgumentException("Course code is required!");
		}
		
		Optional<CourseDto> existingCourse = courseService.getByCode(courseDto.getCode());
		if (existingCourse.isPresent()) {
			throw new IllegalArgumentException("Course code is duplicating!");
		}
		
		CourseDto createdCourse = courseService.save(courseDto);
		ResponseDto<CourseDto> responseDto = new ResponseDto<>("Course created successfully!", "", createdCourse);
		return ResponseEntity.ok(responseDto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseDto<CourseDto>> updateCourse(@PathVariable(name = "id") Long id, @RequestBody CourseDto courseDto) {
		try {
			CourseDto updatedCourse = courseService.update(id, courseDto);
			ResponseDto<CourseDto> responseDto = new ResponseDto<>("Course updated successfully!", "", updatedCourse);
			
			return ResponseEntity.ok(responseDto);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDto<Void>> deleteCourse(@PathVariable(name = "id") Long id) {
		courseService.deleteById(id);
		ResponseDto<Void> responseDto = new ResponseDto<>("Course deleted successfully!", "", null);
		return ResponseEntity.ok(responseDto);
	}
	
}
