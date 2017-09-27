package com.haiboyan.organization.repository;

import com.haiboyan.organization.model.Employee;
import com.haiboyan.organization.model.Team;
import org.springframework.data.repository.CrudRepository;


public interface TeamRepository extends CrudRepository<Team, Long> {
    Team findByManagerId(Long id);
}
