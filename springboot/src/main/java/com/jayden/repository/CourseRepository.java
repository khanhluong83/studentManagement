package com.jayden.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jayden.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

	@Query("select c from Course c where c.code = :code")
	Course findByCode(@Param("code") String code);
	
	@Query("select c from Course c where c.id in :idList")
	List<Course> findByIdList(@Param("idList") List<Long> idList);
	
}
