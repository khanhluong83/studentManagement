package com.jayden.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.jayden.dto.StudentDto;
import com.jayden.entity.Course;
import com.jayden.entity.Student;
import com.jayden.repository.CourseRepository;
import com.jayden.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

	@Mock
    private StudentRepository studentRepository;
	
	@Mock
    private CourseRepository courseRepository;
	
	@Spy
	private ModelMapper modelMapper = new ModelMapper();
	
	@InjectMocks
	private StudentServiceImpl studentService;
	
	@Test
	public void testSave_UserWithNoCourse() {
		Student testStudent = generateSampleStudents().get(0);
		StudentDto testStudentDto = modelMapper.map(testStudent, StudentDto.class);
		when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
		
		var result = studentService.save(testStudentDto);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(testStudent.getFirstName(), result.getFirstName());
		Assertions.assertNull(result.getCourseIdList());
	}
	
	@Test
	public void testSave_UserWithCourse() {
		List<Course> sampleCourses = generateSampleCourses();
		when(courseRepository.findByIdList(any(List.class))).thenReturn(sampleCourses);
		
		Course testCourse = sampleCourses.get(0);
		
		Student testStudent = generateSampleStudents().get(0);
		StudentDto testStudentDto = modelMapper.map(testStudent, StudentDto.class);
		testStudentDto.setCourseIdList(List.of(testCourse.getId()));
		testStudent.setCourses(Set.of(testCourse));
		when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
		
		var result = studentService.save(testStudentDto);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(testStudent.getFirstName(), result.getFirstName());
		Assertions.assertNotNull(result.getCourseIdList());
		Assertions.assertEquals(1, result.getCourseIdList().size());
	}
	
	@Test
	public void testUpdate_UserWithNoCourse() {
		Student testStudent = generateSampleStudents().get(0);
		StudentDto testStudentDto = modelMapper.map(testStudent, StudentDto.class);
		when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
		when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testStudent));
		
		var result = studentService.update(1l, testStudentDto);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(testStudent.getFirstName(), result.getFirstName());
		Assertions.assertNull(result.getCourseIdList());
	}
	
	@Test
	public void testUpdate_UserWithCourse() {
		List<Course> sampleCourses = generateSampleCourses();
		when(courseRepository.findByIdList(any(List.class))).thenReturn(sampleCourses);
		
		Course testCourse = sampleCourses.get(0);
		
		Student testStudent = generateSampleStudents().get(0);
		StudentDto testStudentDto = modelMapper.map(testStudent, StudentDto.class);
		testStudentDto.setCourseIdList(List.of(testCourse.getId()));
		testStudent.getCourses().add(testCourse);
		when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
		when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testStudent));
		
		var result = studentService.update(1l, testStudentDto);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(testStudent.getFirstName(), result.getFirstName());
		Assertions.assertNotNull(result.getCourseIdList());
		Assertions.assertEquals(sampleCourses.size(), result.getCourseIdList().size());
	}
	
	private List<StudentDto> generateSampleStudentDtos() {
		List<StudentDto> sampleStudentDtos = new ArrayList<>();
		StudentDto sampleStudentDto = new StudentDto();
		sampleStudentDto.setId(1l);
		sampleStudentDto.setFirstName("Sample");
		sampleStudentDto.setLastName("Student 1");
		sampleStudentDto.setEmail("sampleStudent1@gmail.com");
		sampleStudentDto.setPhone("1122334455");
		sampleStudentDtos.add(sampleStudentDto);
		
		StudentDto sampleStudentDto2 = new StudentDto();
		sampleStudentDto2.setId(2l);
		sampleStudentDto2.setFirstName("Sample");
		sampleStudentDto2.setLastName("Student 2");
		sampleStudentDto2.setEmail("sampleStudent2@gmail.com");
		sampleStudentDto2.setPhone("2233334455");
		sampleStudentDtos.add(sampleStudentDto2);
		
		return sampleStudentDtos;
	}
	
	private List<Student> generateSampleStudents() {
		List<Student> sampleStudents = new ArrayList<>();
		Student sampleStudent = new Student();
		sampleStudent.setId(1l);
		sampleStudent.setFirstName("Sample");
		sampleStudent.setLastName("Student 1");
		sampleStudent.setEmail("sampleStudent1@gmail.com");
		sampleStudent.setPhone("1122334455");
		sampleStudents.add(sampleStudent);
		
		Student sampleStudent2 = new Student();
		sampleStudent2.setId(2l);
		sampleStudent2.setFirstName("Sample");
		sampleStudent2.setLastName("Student 2");
		sampleStudent2.setEmail("sampleStudent2@gmail.com");
		sampleStudent2.setPhone("2233334455");
		sampleStudents.add(sampleStudent2);
		
		return sampleStudents;
	}
	
	private List<Course> generateSampleCourses() {
		List<Course> sampleCourses = new ArrayList<>();
		Course sampleCourse = new Course();
		sampleCourse.setId(1l);
		sampleCourse.setCode("Code 1");
		sampleCourse.setName("Course 1");
		sampleCourse.setStartDate(new Date());
		sampleCourse.setEndDate(new Date());
		sampleCourses.add(sampleCourse);
		
		Course sampleCourse2 = new Course();
		sampleCourse2.setId(2l);
		sampleCourse2.setCode("Code 2");
		sampleCourse2.setName("Course 2");
		sampleCourse2.setStartDate(new Date());
		sampleCourse2.setEndDate(new Date());
		sampleCourses.add(sampleCourse2);
		
		return sampleCourses;
	}
	
}
