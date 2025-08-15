package com.jayden.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.jayden.entity.Course;
import com.jayden.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
	
	@Mock
    private CourseRepository courseRepository;
	
	@Spy
	private ModelMapper modelMapper = new ModelMapper();
	
	@InjectMocks
	private CourseServiceImpl courseService;
	
	@BeforeEach
	public void init() {
		
	}

	@Test
	public void testSearchAll_PageRequestNull() {
		List<Course> sampleCourses = generateSampleCourses();
		
		when(courseRepository.findAll()).thenReturn(sampleCourses);
		
		var result = courseService.searchAll(null);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.size());
		verify(courseRepository).findAll();
	}
	
	@Test
	public void testSearchAll_FirstTenRecords() {
		List<Course> sampleCourses = generateSampleCourses();
		
		PageRequest firstPageRequest = PageRequest.of(0, 10);
		Page<Course> mockedPage = new PageImpl<>(sampleCourses, Pageable.unpaged(), sampleCourses.size());
		when(courseRepository.findAll(firstPageRequest)).thenReturn(mockedPage);
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		var result = courseService.searchAll(pageRequest);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.size());
		verify(courseRepository).findAll(firstPageRequest);
	}
	
	@Test
	public void testSearchAll_SecondTenRecords() {
		PageRequest secondPageRequest = PageRequest.of(1, 10);
		Page<Course> emptyMockedPage = new PageImpl<>(new ArrayList<>(), Pageable.unpaged(), 0);
		when(courseRepository.findAll(secondPageRequest)).thenReturn(emptyMockedPage);
		
		PageRequest pageRequest = PageRequest.of(1, 10);
		
		var result = courseService.searchAll(pageRequest);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(0, result.size());
		verify(courseRepository).findAll(secondPageRequest);
	}
	
	@Test
	public void testGetById_IdExisting() {
		List<Course> sampleCourses = generateSampleCourses();
		
		when(courseRepository.findById(eq(1l))).thenReturn(Optional.of(sampleCourses.get(0)));
		
		var result = courseService.getById(1l);
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isPresent());
		verify(courseRepository).findById(1l);
	}
	
	@Test
	public void testGetById_IdNotExisting() {
		
		when(courseRepository.findById(eq(10l))).thenReturn(Optional.ofNullable(null));
		
		var result = courseService.getById(10L);
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isPresent());
		verify(courseRepository).findById(10l);
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
