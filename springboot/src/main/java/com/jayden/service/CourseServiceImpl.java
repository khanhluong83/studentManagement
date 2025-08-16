package com.jayden.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jayden.dto.CourseDto;
import com.jayden.entity.Course;
import com.jayden.repository.CourseRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long countAll() {
		return courseRepository.count();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CourseDto> searchAll(Pageable pageable) {
		if (pageable != null) {
			return courseRepository.findAll(pageable).stream()
					.map(this::convertToDto)
					.collect(Collectors.toList());
		} else {
			return courseRepository.findAll().stream()
					.map(this::convertToDto)
					.collect(Collectors.toList());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<CourseDto> getById(Long id) {
		return courseRepository.findById(id).map(this::convertToDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<CourseDto> getByCode(String code) {
		return Optional.ofNullable(courseRepository.findByCode(code)).map(this::convertToDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CourseDto save(CourseDto courseDto) {
		Course course = convertToEntity(courseDto);
		Course savedCourse = courseRepository.save(course);
		return convertToDto(savedCourse);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = EntityNotFoundException.class)
	public CourseDto update(Long id, CourseDto courseDto) {
		Course course = courseRepository.findById(id).orElseThrow();
		course.setName(courseDto.getName());
		
		Course updatedCourse = courseRepository.save(course);
		return convertToDto(updatedCourse);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteById(Long id) {
		courseRepository.deleteById(id);
	}
	
	private CourseDto convertToDto(Course course) {
		return modelMapper.map(course, CourseDto.class);
	}
	
	private Course convertToEntity(CourseDto courseDto) {
		return modelMapper.map(courseDto, Course.class);
	}

}
