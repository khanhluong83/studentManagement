package com.jayden.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jayden.dto.StudentDto;
import com.jayden.dto.StudentSearchDto;
import com.jayden.entity.Course;
import com.jayden.entity.Student;
import com.jayden.repository.CourseRepository;
import com.jayden.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long countAll(StudentSearchDto searchDto) {
		Student searchStudent = new Student();
		searchStudent.setFirstName(searchDto.getFirstName());
		searchStudent.setLastName(searchDto.getLastName());
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("firstName", match -> match.contains().ignoreCase())
				.withMatcher("lastName", match -> match.contains().ignoreCase());
		
		Example<Student> example = Example.of(searchStudent, exampleMatcher);
		
		return studentRepository.count(example);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<StudentDto> searchAll(StudentSearchDto searchDto, Pageable pageable) {
		
		Student searchStudent = new Student();
		searchStudent.setFirstName(searchDto.getFirstName());
		searchStudent.setLastName(searchDto.getLastName());
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("firstName", match -> match.contains().ignoreCase())
				.withMatcher("lastName", match -> match.contains().ignoreCase());
		
		Example<Student> example = Example.of(searchStudent, exampleMatcher);
		
		return studentRepository.findAll(example, pageable).stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<StudentDto> getById(Long id) {
		return studentRepository.findById(id).map(this::convertToDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<StudentDto> getByEmail(String email) {
		return Optional.ofNullable(studentRepository.findByEmail(email)).map(this::convertToDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public StudentDto save(StudentDto studentDto) {
		Student student = convertToEntity(studentDto);
		if (studentDto.getCourseIdList() != null && studentDto.getCourseIdList().size() > 0) {
			List<Course> courses = courseRepository.findByIdList(studentDto.getCourseIdList());
			student.getCourses().addAll(courses);
		}
		Student savedStudent = studentRepository.save(student);
		return convertToDto(savedStudent);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = EntityNotFoundException.class)
	public StudentDto update(Long id, StudentDto studentDto) {
		Student student = studentRepository.findById(id).orElseThrow();
		student.setFirstName(studentDto.getFirstName());
		student.setLastName(studentDto.getLastName());
		student.setPhone(studentDto.getPhone());
		
		if (student.getCourses() != null && student.getCourses().size() > 0) {
			student.getCourses().clear();
		}
		
		if (studentDto.getCourseIdList() != null && studentDto.getCourseIdList().size() > 0) {
			List<Course> courses = courseRepository.findByIdList(studentDto.getCourseIdList());
			student.getCourses().addAll(courses);
		}
		
		Student updatedStudent = studentRepository.save(student);
		return convertToDto(updatedStudent);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteById(Long id) {
		studentRepository.deleteById(id);
	}
	
	private StudentDto convertToDto(Student student) {
		StudentDto studentDto = modelMapper.map(student, StudentDto.class);
		if (student.getCourses() != null && student.getCourses().size() > 0) {
//			List<Long> courseIdList = student.getCourses().stream()
//					.map(c -> c.getId())
//					.collect(Collectors.toList());
//			studentDto.setCourseIdList(courseIdList);
			
			student.getCourses().forEach(c -> {
				studentDto.getCourseIdList().add(c.getId());
				studentDto.getCourseNameList().add(c.getName());
			});
		}
		return studentDto;
	}
	
	private Student convertToEntity(StudentDto studentDto) {
		return modelMapper.map(studentDto, Student.class);
	}
	
}
