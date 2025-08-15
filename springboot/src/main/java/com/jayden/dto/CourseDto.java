package com.jayden.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CourseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 633530284764667735L;
	
	private Long id;
	private String code;
	private String name;
	private Date startDate;
	private Date endDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(code, id, name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseDto other = (CourseDto) obj;
		return Objects.equals(code, other.code) && Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
	
	@Override
	public String toString() {
		return "CourseDto [id=" + id + ", code=" + code + ", name=" + name + ", startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}
		
}
