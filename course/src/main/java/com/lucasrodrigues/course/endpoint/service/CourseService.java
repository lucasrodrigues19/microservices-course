package com.lucasrodrigues.course.endpoint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lucasrodrigues.core.model.Course;
import com.lucasrodrigues.core.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseService {

    
	private final CourseRepository courseRepository;
    
	
	public Iterable<Course> list(Pageable pageable){
		log.info("Listando todos os cusos");
		return courseRepository.findAll(pageable);
	}
}
