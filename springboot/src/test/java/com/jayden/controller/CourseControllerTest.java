package com.jayden.controller;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayden.dto.CourseDto;
import com.jayden.service.CourseService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("unitTest")
public class CourseControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CourseService courseService;
	
	@Test
	public void testLoadSearch_ValidInput() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/course")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
		;
	}
	
	@Test
	public void testGetCourseById_Existing() throws Exception {
		
		CourseDto courseDto = new CourseDto();
		courseDto.setCode("Test Get Code 1");
		courseDto.setName("Test Get Name 1");
		courseDto.setStartDate(new Date());
		courseDto.setEndDate(new Date());
		
		CourseDto savedCourse = courseService.save(courseDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/course/" + savedCourse.getId())
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
		;
		
	}
	
	@Test
	public void testGetCourseById_NotExisting() throws Exception {
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/course/-1")
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//			.andExpect(MockMvcResultMatchers.content().string(StringStartsWith.startsWith("Entity not found")))
		;
		
	}
	
	@Test
	public void testCreateCourse_ValidInput() throws Exception {
		
		CourseDto courseDto = new CourseDto();
		courseDto.setCode("Test Code 1");
		courseDto.setName("Test Name 1");
		courseDto.setStartDate(new Date());
		courseDto.setEndDate(new Date());
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(courseDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/course")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andExpect(MockMvcResultMatchers.status().is(200))
		;
		
		Optional<CourseDto> result = courseService.getByCode(courseDto.getCode());
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(courseDto.getCode(), result.get().getCode());
		Assertions.assertEquals(courseDto.getName(), result.get().getName());
		
	}
	
	@Test
	public void testCreateCourse_InValidInput_MissingCode() throws Exception {
		
		CourseDto courseDto = new CourseDto();
		courseDto.setCode("");
		courseDto.setName("Test Name 1");
		courseDto.setStartDate(new Date());
		courseDto.setEndDate(new Date());
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(courseDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/course")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andExpect(MockMvcResultMatchers.content().string("Invalid argument: Course code is required!"))
		;
		
	}
	
	@Test
	public void testCreateCourse_InValidInput_DuplicatingCode() throws Exception {
		
		CourseDto courseDto = new CourseDto();
		courseDto.setCode("Test Dup Code 1");
		courseDto.setName("Test Dup Name 1");
		courseDto.setStartDate(new Date());
		courseDto.setEndDate(new Date());
		
		courseService.save(courseDto);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(courseDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/course")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
			.andExpect(MockMvcResultMatchers.content().string("Invalid argument: Course code is duplicating!"))
		;
		
	}
	
	@Test
	public void testUpdateCourse_ValidInput() throws Exception {
		
		CourseDto courseDto = new CourseDto();
		courseDto.setCode("Test Update Code 1");
		courseDto.setName("Test Update Name 1");
		courseDto.setStartDate(new Date());
		courseDto.setEndDate(new Date());
		
		CourseDto createdCourseDto = courseService.save(courseDto);
		
		CourseDto updateCourseDto = new CourseDto();
		updateCourseDto.setName("Test Update Name 1 - updated name");
		updateCourseDto.setStartDate(new Date());
		updateCourseDto.setEndDate(new Date());
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateCourseDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.put("/course/" + createdCourseDto.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
		;
		
		Optional<CourseDto> result = courseService.getById(createdCourseDto.getId());
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(updateCourseDto.getName(), result.get().getName());
	}
	
	@Test
	public void testUpdateCourse_NotExisting() throws Exception {
		CourseDto updateCourseDto = new CourseDto();
		updateCourseDto.setName("Test Update Name 1 - updated name");
		updateCourseDto.setStartDate(new Date());
		updateCourseDto.setEndDate(new Date());
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateCourseDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.put("/course/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
		;
	}
	
	@Test
	public void testDeleteCourse_NotExisting() throws Exception {
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/course/-1")
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
		;
	}
	
	@Test
	public void testDeleteCourse_Existing() throws Exception {
		
		CourseDto courseDto = new CourseDto();
		courseDto.setCode("Test Delete Code 1");
		courseDto.setName("Test Delete Name 1");
		courseDto.setStartDate(new Date());
		courseDto.setEndDate(new Date());
		
		CourseDto createdCourseDto = courseService.save(courseDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/course/" + createdCourseDto.getId())
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
		;
		
		Optional<CourseDto> result = courseService.getById(createdCourseDto.getId());
		
		Assertions.assertFalse(result.isPresent());
		
	}
	
}
