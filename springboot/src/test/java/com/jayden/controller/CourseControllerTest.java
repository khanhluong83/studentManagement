package com.jayden.controller;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayden.dto.CourseDto;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("unitTest")
public class CourseControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testLoadSearch_ValidInput() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/course")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
		;
	}
	
	@Test
	public void testCreateApplication_ValidInput() throws Exception {
		
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
		
	}
	
	@Test
	public void testCreateApplication_InValidInput_MissingCode() throws Exception {
		
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
	
}
