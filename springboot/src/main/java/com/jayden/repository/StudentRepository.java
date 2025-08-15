package com.jayden.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jayden.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("select s from Student s where s.email = :email")
	Student findByEmail(@Param("email") String email);
	
}
