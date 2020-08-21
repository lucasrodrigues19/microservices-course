package com.lucasrodrigues.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lucasrodrigues.core.model.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {

}
