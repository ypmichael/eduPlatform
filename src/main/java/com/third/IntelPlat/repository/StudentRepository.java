package com.third.IntelPlat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.StudentsEntity;

@Component
public interface StudentRepository extends CrudRepository<StudentsEntity, Integer>
{
	

}
