package com.jayden.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8858692297650952469L;

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private List<Long> courseIdList = new ArrayList<>();
	private List<String> courseNameList = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public List<Long> getCourseIdList() {
		return courseIdList;
	}
	public void setCourseIdList(List<Long> courseIdList) {
		this.courseIdList = courseIdList;
	}
	
	public List<String> getCourseNameList() {
		return courseNameList;
	}
	public void setCourseNameList(List<String> courseNameList) {
		this.courseNameList = courseNameList;
	}
	@Override
	public int hashCode() {
		return Objects.hash(email, id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentDto other = (StudentDto) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "StudentDto [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + "]";
	}
	
}
