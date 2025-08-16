package com.jayden.controller;

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
import com.jayden.dto.StudentDto;
import com.jayden.service.CourseService;
import com.jayden.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("unitTest")
public class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private StudentService studentService;
	
	@Test
	public void testLoadSearch_ValidInput() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/student")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
		;
	}
	
	@Test
	public void testGetById_Existing() throws Exception {
		
		StudentDto studentDto = new StudentDto();
		studentDto.setFirstName("Test Get First Name 1");
		studentDto.setLastName("Test Get Last Name 1");
		studentDto.setEmail("Test Get Email 1");
		studentDto.setPhone("Test Get Phone 1");
		
		StudentDto savedStudent = studentService.save(studentDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/student/" + savedStudent.getId())
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
		;
		
	}
	
	@Test
	public void testGetById_NotExisting() throws Exception {
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/student/-1")
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//			.andExpect(MockMvcResultMatchers.content().string(StringStartsWith.startsWith("Entity not found")))
		;
		
	}
	
	@Test
	public void testCreate_ValidInput() throws Exception {
		
		StudentDto studentDto = new StudentDto();
		studentDto.setFirstName("Test Create First Name 1");
		studentDto.setLastName("Test Create Last Name 1");
		studentDto.setEmail("Test Create Email 1");
		studentDto.setPhone("Test Create Phone 1");
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(studentDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/student")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andExpect(MockMvcResultMatchers.status().is(200))
		;
		
		Optional<StudentDto> result = studentService.getByEmail(studentDto.getEmail());
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(studentDto.getEmail(), result.get().getEmail());
		Assertions.assertEquals(studentDto.getFirstName(), result.get().getFirstName());
		
	}
	
	@Test
	public void testCreate_InValidInput_MissingEmail() throws Exception {
		
		StudentDto studentDto = new StudentDto();
		studentDto.setFirstName("Test Create First Name 1");
		studentDto.setLastName("Test Create Last Name 1");
		studentDto.setEmail("");
		studentDto.setPhone("Test Create Phone 1");
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(studentDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/student")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andExpect(MockMvcResultMatchers.content().string("Invalid argument: Student email is required!"))
		;
		
	}
	
	@Test
	public void testCreate_InValidInput_DuplicatingEmail() throws Exception {
		
		StudentDto studentDto = new StudentDto();
		studentDto.setFirstName("Test Dup First Name 1");
		studentDto.setLastName("Test Dup Last Name 1");
		studentDto.setEmail("Test Dup Email 1");
		studentDto.setPhone("Test Dup Phone 1");
		
		studentService.save(studentDto);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(studentDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/student")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
			.andExpect(MockMvcResultMatchers.content().string("Invalid argument: Student email is duplicating!"))
		;
		
	}
	
	@Test
	public void testUpdate_ValidInput() throws Exception {
		
		StudentDto studentDto = new StudentDto();
		studentDto.setFirstName("Test Update First Name 1");
		studentDto.setLastName("Test Update Last Name 1");
		studentDto.setEmail("Test Update Email 1");
		studentDto.setPhone("Test Update Phone 1");
		
		StudentDto createdStudentDto = studentService.save(studentDto);
		
		StudentDto updateStudentDto = new StudentDto();
		updateStudentDto.setFirstName("Test Update First Name 1 - updated");
		updateStudentDto.setLastName("Test Update Last Name 1 - updated");
		updateStudentDto.setPhone("Test Update Phone 1 - updated");
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateStudentDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.put("/student/" + createdStudentDto.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
		;
		
		Optional<StudentDto> result = studentService.getById(createdStudentDto.getId());
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(updateStudentDto.getFirstName(), result.get().getFirstName());
		Assertions.assertEquals(updateStudentDto.getLastName(), result.get().getLastName());
		Assertions.assertEquals(updateStudentDto.getPhone(), result.get().getPhone());
	}
	
	@Test
	public void testUpdate_NotExisting() throws Exception {
		StudentDto updateStudentDto = new StudentDto();
		updateStudentDto.setFirstName("Test Update First Name 1 - updated");
		updateStudentDto.setLastName("Test Update Last Name 1 - updated");
		updateStudentDto.setPhone("Test Update Phone 1 - updated");
		
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updateStudentDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.put("/student/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
		;
	}
	
	@Test
	public void testDelete_NotExisting() throws Exception {
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/student/-1")
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
		;
	}
	
	@Test
	public void testDelete_Existing() throws Exception {
		
		StudentDto studentDto = new StudentDto();
		studentDto.setFirstName("Test Delete First Name 1");
		studentDto.setLastName("Test Delete Last Name 1");
		studentDto.setEmail("Test Delete Email 1");
		studentDto.setPhone("Test Delete Phone 1");
		
		StudentDto createdStudentDto = studentService.save(studentDto);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/student/" + createdStudentDto.getId())
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
		;
		
		Optional<StudentDto> result = studentService.getById(createdStudentDto.getId());
		
		Assertions.assertFalse(result.isPresent());
		
	}
	
	
	
}
