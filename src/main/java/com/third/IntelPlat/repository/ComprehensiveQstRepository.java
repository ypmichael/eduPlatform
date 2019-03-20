package com.third.IntelPlat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.ComprehensiveQst;


@Component
public interface ComprehensiveQstRepository extends CrudRepository<ComprehensiveQst, Integer>
{
	

}
