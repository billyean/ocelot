package com.haiboyan.organization.controller;

import com.haiboyan.organization.exception.MultipleCEOException;
import com.haiboyan.organization.exception.NoSuchEmployeeException;
import com.haiboyan.organization.exception.NoSuchTeamException;
import com.haiboyan.organization.exception.NotAuthroziedPromotionException;
import com.haiboyan.organization.model.Action;
import com.haiboyan.organization.model.Employee;
import com.haiboyan.organization.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
        try {
            Employee created = employeeService.onCreateEmployee(employee);
            return new ResponseEntity(created, HttpStatus.CREATED);
        } catch (MultipleCEOException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Employee> getAllEmployees() {
        return employeeService.allEmployees();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{employeeId}/direct")
    Collection<Employee> getAllDirect(@PathVariable Long employeeId) {
        return employeeService.allDirectReports(employeeId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{employeeId}/action")
    ResponseEntity<?> promoteEmployee(@PathVariable Long employeeId, @RequestBody Action action) {
        try {
            switch (action.getName()) {
                case "promote":
                    employeeService.promote(employeeId);
                    return new ResponseEntity("Employee moved to new team", HttpStatus.OK);
                case "onLeave":
                    employeeService.onLeave(employeeId);
                    return new ResponseEntity("Employee is on leave", HttpStatus.OK);
                case "comBack":
                    employeeService.onLeaveFinished(employeeId);
                    return new ResponseEntity("Employee done on leave", HttpStatus.OK);
                default:
                    return new ResponseEntity("Invalid action", HttpStatus.FORBIDDEN);
            }
        } catch (MultipleCEOException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (NoSuchEmployeeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotAuthroziedPromotionException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{employeeId}/team/{teamId}")
    ResponseEntity<?> switchEmployeeTeam(@PathVariable Long employeeId, @PathVariable Long teamId) {
        try {
            employeeService.moveNewTeam(employeeId, teamId);
            return new ResponseEntity("Employee moved to new team", HttpStatus.OK);
        } catch (NoSuchEmployeeException | NoSuchTeamException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
