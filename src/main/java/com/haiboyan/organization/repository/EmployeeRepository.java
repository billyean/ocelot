package com.haiboyan.organization.repository;

import com.haiboyan.organization.model.Employee;
import com.haiboyan.organization.model.Role;
import com.haiboyan.organization.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, CrudRepository<Employee, Long> {
    Collection<Employee> findByTeam(Team team);

    Collection<Employee> findByTempTeam(Team team);

    Collection<Employee> findByRole(Role role);
}
