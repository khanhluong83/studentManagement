package com.jayden.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.jayden.dto.StudentDto;
import com.jayden.dto.CourseDto;
import com.jayden.dto.ResponseDto;
import com.jayden.dto.SearchResultDto;
import com.jayden.service.StudentService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@GetMapping
	public ResponseEntity<SearchResultDto<StudentDto>> getAllStudents(@PageableDefault(size = 10) Pageable pageable) {
		long studentCount = studentService.countAll();
		List<StudentDto> studentList = studentService.searchAll(pageable);
		var searchResultDto = new SearchResultDto<StudentDto>(studentCount, studentList);
		
		return ResponseEntity.ok(searchResultDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<StudentDto> getStudentById(@PathVariable(name = "id") Long id) {
		Optional<StudentDto> student = studentService.getById(id);
		
		return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<ResponseDto<StudentDto>> createStudent(@RequestBody StudentDto studentDto) {
		if (studentDto.getEmail() == null || StringUtils.isBlank(studentDto.getEmail())) {
			throw new IllegalArgumentException("Student email is required!");
		}
		
		Optional<StudentDto> existingStudent = studentService.getByEmail(studentDto.getEmail());
		if (existingStudent.isPresent()) {
			throw new IllegalArgumentException("Student email is duplicating!");
		}
		
		StudentDto createdStudent = studentService.save(studentDto);
		ResponseDto<StudentDto> responseDto = new ResponseDto<>("Student created successfully!", "", createdStudent);
		return ResponseEntity.ok(responseDto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseDto<StudentDto>> updateStudent(@PathVariable(name = "id") Long id, @RequestBody StudentDto studentDto) {
		try {
			StudentDto updatedStudent = studentService.update(id, studentDto);
			ResponseDto<StudentDto> responseDto = new ResponseDto<>("Student updated successfully!", "", updatedStudent);
			
			return ResponseEntity.ok(responseDto);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDto<Void>> deleteStudent(@PathVariable(name = "id") Long id) {
		Optional<StudentDto> existingStudent = studentService.getById(id);
		if (!existingStudent.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		studentService.deleteById(id);
		ResponseDto<Void> responseDto = new ResponseDto<>("Student deleted successfully!", "", null);
		return ResponseEntity.ok(responseDto);
	}
}
