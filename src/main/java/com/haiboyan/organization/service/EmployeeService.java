package com.haiboyan.organization.service;

import com.haiboyan.organization.exception.*;
import com.haiboyan.organization.model.Employee;
import com.haiboyan.organization.model.Role;
import com.haiboyan.organization.model.State;
import com.haiboyan.organization.model.Team;
import com.haiboyan.organization.repository.EmployeeRepository;
import com.haiboyan.organization.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Create employee in database.
     *
     * @param employee
     * @return
     * @throws MultipleCEOException if trying to create a CEO role employee but CEO existed already.
     */
    public Employee onCreateEmployee(Employee employee) throws MultipleCEOException {
        if (employee.getRole() == Role.CEO) {
            // Find all in position or on leave ceo.
            List<Employee> ceos = employeeRepository.findByRole(Role.CEO).stream()
                    .filter(e -> e.getState().valid()).collect(toList());
            if (ceos.size() != 0) {
                throw new MultipleCEOException();
            }
        }
        return employeeRepository.save(employee);
    }

    public void onCreateTeam(Team team) {
        teamRepository.save(team);
    }

    /**
     * Get employee by given id.
     *
     * @param employeeId employee id
     * @return
     */
    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findOne(employeeId);
    }


    /**
     * Get employee's manager by given id.
     *
     * @param employeeId employee id
     * @return
     */
    public Employee getEmployeeManager(Long employeeId) {
        Employee employee =  employeeRepository.findOne(employeeId);
        if (employee == null) {
            return null;
        }

        Team team = teamRepository.findOne(employee.getTeam().getId());
        return team.getManager();
    }

    /**
     * Get all valid employees in organization
     * @return
     */
    public Set<Employee> allEmployees() {
        return employeeRepository.findAll().stream().filter(Employee::isValid).collect(toSet());
    }

    /**
     * Get all direct reports by given employee id.
     * 1. Find managed team by this employee.
     * 2. Find all employee whose state is in position and team belongs tro the team found in step 1.
     * 3. Find all employee whose state is in position and temp team belongs tro the team found in step 1.
     * 4. Combine step 2 and step 3 as result.
     *
     * @param employeeId
     * @return
     */
    public Set<Employee> allDirectReports(Long employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null)
            return new HashSet<>();
        Team team = teamRepository.findByManagerId(employeeId);

        if (null == team) {
            return new HashSet<>();
        }
        Set<Employee> directReports = employeeRepository.findByTeam(team).stream().filter(Employee::isInPostion).collect(toSet());
        Set<Employee> directTempReports = employeeRepository.findByTempTeam(team).stream().filter(Employee::isInPostion).collect(toSet());
        directReports.addAll(directTempReports);
        return directReports;
    }

    /**
     * When an employee goes on holidays:
     * 1. The employee will have on leave state.
     * 2. all his subordinates have temp team to the on holiday employe's team .
     *
     * @param employeeId employee id who goes on holiday.
     * @throws InvalidStateChangeException
     * @throws NoSuchEmployeeException
     */
    public void onLeave(Long employeeId) throws InvalidStateChangeException, NoSuchEmployeeException {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null)
            throw new NoSuchEmployeeException(employeeId);
        stateChange(employee, State.ON_LEAVE);
        Team team = employee.getTeam();
        Set<Employee> directReports = directReport(employee);
        for (Employee directReport: directReports) {
            directReport.setTempTeam(team);
            employeeRepository.save(directReport);
        }
    }

    /**
     * When an employee comes back from holidays,:
     * 1. The employee will have in position state.
     * 2. all his subordinates have temp team will remove their temp team.
     *
     * @param employeeId employee id who comes back from holiday.
     * @throws InvalidStateChangeException
     * @throws NoSuchEmployeeException
     */
    public void onLeaveFinished(Long employeeId) throws InvalidStateChangeException, NoSuchEmployeeException {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null)
            throw new NoSuchEmployeeException(employeeId);
        stateChange(employee, State.IN_POSITION);
        Set<Employee> directReports = directReport(employee);
        for (Employee directReport: directReports) {
            directReport.setTempTeam(null);
            employeeRepository.save(directReport);
        }
    }

    private void stateChange(Employee employee, State newState) throws InvalidStateChangeException {
        if (!employee.getState().validNewState(newState)) {
            throw new InvalidStateChangeException(employee.getState(), newState);
        }
        employee.setState(newState);
        employeeRepository.save(employee);
    }

    public void onQuit(Employee employee) {
        employee.setState(State.QUITTED);
        employeeRepository.save(employee);
        Team team = teamRepository.findByManagerId(employee.getId());
        // Since manager quit, no manager in the team
        // We could make the most senior as the manager by default, but better leave it for promotion.
        team.setManager(null);
        teamRepository.save(team);
    }

    public Set<Employee> allReport(Employee employee) {
        Team team = teamRepository.findByManagerId(employee.getId());
        Set<Employee> reports = new HashSet<>(employeeRepository.findByTeam(team));

        Set<Employee> onLeaveManagers = reports.stream().
                filter(e -> e.getRole().isManager() && e.getState() != State.ON_LEAVE).collect(toSet());
        reports.removeAll(onLeaveManagers);

        if (onLeaveManagers.size() != 0) {
            for (Employee onLeaveManager: onLeaveManagers) {
                reports.addAll(allReport(onLeaveManager));
            }
        }

        return reports;
    }

    private Set<Employee> directReport(Employee employee) {
        Team team = teamRepository.findByManagerId(employee.getId());
        return new HashSet<>(employeeRepository.findByTeam(team));
    }

    /**
     * Find most senior reports based on earliest hire date.
     * @param team
     * @return
     */
    private Optional<Employee> mostSeniorPermanent(Team team) {
        Collection<Employee> employees = employeeRepository.findByTeam(team);
        return employees.stream().filter(Employee::isPermanent).min(Comparator.comparing(Employee::getHireDate));
    }

    /**
     * Move the team. An employee can move to another team  within the organisation. When moving happened,
     * 1. The moving employee will have new team associated.
     * 2. Find the most senior in his team, move the most senior to moving employee's current team.
     * 3. If the most senior is managed a team already. promote managed team as well.
     *
     * @param employeeId moving employee's id
     * @param newTeamId moving team's id
     * @throws NoSuchEmployeeException no employee by given employee id
     * @throws NoSuchTeamException no team found by given id
     */
    public void moveNewTeam(Long employeeId, Long newTeamId) throws NoSuchEmployeeException, NoSuchTeamException {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null)
            throw new NoSuchEmployeeException(employeeId);
        Team newTeam = teamRepository.findOne(newTeamId);
        Team currentTeam = employee.getTeam();
        Team currentManaged = teamRepository.findByManagerId(employeeId);
        if (newTeam == null)
            throw new NoSuchTeamException(newTeamId);

        employee.setTeam(newTeam);
        employeeRepository.save(employee);

        // Promote most senior one as the manager
        if (currentManaged != null) {
            Optional<Employee> optional = mostSeniorPermanent(currentManaged);

            if (optional.isPresent()) {
                Employee mostSenior = optional.get();

                moveNewTeam(mostSenior.getId(), currentTeam.getId());
                if (currentManaged != null) {
                    currentManaged.setManager(mostSenior);
                    teamRepository.save(currentTeam);
                }
            } else {
                if (currentManaged != null) {
                    currentManaged.setManager(null);
                    teamRepository.save(currentTeam);
                }
            }
        }
    }

    /**
     *
     * @param employeeId
     * @throws NoSuchEmployeeException
     * @throws MultipleCEOException
     * @throws NotAuthroziedPromotionException
     */
    public void promote(Long employeeId) throws NoSuchEmployeeException, MultipleCEOException, NotAuthroziedPromotionException {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null)
            throw new NoSuchEmployeeException(employeeId);

        Role nextLevel = employee.getRole().nextLevel();
        Set<Employee> allReports = allReport(employee);
        Set<Employee> directReports = directReport(employee);

        int numOfReports = allReport(employee).size();
        Set<Employee> allDirectManagers
                = directReports.stream().filter(Employee::isManager).collect(toSet());

        switch (nextLevel) {
            case CEO:
                throw new MultipleCEOException();
            case VP:
                if(allReports.size() < 40 || allDirectManagers.size() < 4) {
                    throw new NotAuthroziedPromotionException("To be promoted to VP needs at least 40 employees in his organisation including at least 4 directors");
                }
            case Director:
                if(allReports.size() < 20 || allDirectManagers.size() < 2) {
                    throw new NotAuthroziedPromotionException("To be promoted to Directors needs at least 20 employees in his organisation including at least 2 managers");
                }
        }
        Employee supervisor = employee.getTeam().getManager();
        employee.setTeam(supervisor.getTeam());
        employee.setRole(nextLevel);
        employeeRepository.save(employee);
    }

}
