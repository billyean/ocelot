package com.haiboyan.organization.controller;

import com.haiboyan.organization.exception.MultipleCEOException;
import com.haiboyan.organization.exception.NoSuchEmployeeException;
import com.haiboyan.organization.exception.NoSuchTeamException;
import com.haiboyan.organization.exception.NotAuthroziedPromotionException;
import com.haiboyan.organization.model.Employee;
import com.haiboyan.organization.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
        try {
            employeeService.onCreateEmployee(employee);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (MultipleCEOException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<?> getAllEmployees() {
        try {
            return new ResponseEntity(employeeService.allEmployees(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{employeeId}/direct")
    ResponseEntity<?> getAllDirect(@PathVariable Long employeeId) {
        try {
            return new ResponseEntity(employeeService.allDirectReports(employeeId), HttpStatus.OK);
        } catch (NoSuchEmployeeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{employeeId}/promote")
    ResponseEntity<?> promoteEmployee(@PathVariable Long employeeId) {
        try {
            employeeService.promote(employeeId);
            return new ResponseEntity("Employee moved to new team", HttpStatus.OK);
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


    @RequestMapping(method = RequestMethod.PUT, value = "/{employeeId}/team/{teamId}")
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

    @RequestMapping(method = RequestMethod.PUT, value = "/{employeeId}/onLeave")
    ResponseEntity<?> onLeave(@PathVariable Long employeeId) {
        try {
            employeeService.onLeave(employeeId);
            return new ResponseEntity("Employee is on leave", HttpStatus.OK);
        } catch (NoSuchEmployeeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{employeeId}/comeBack")
    ResponseEntity<?> onBack(@PathVariable Long employeeId) {
        try {
            employeeService.onLeaveFinished(employeeId);
            return new ResponseEntity("Employee is on leave", HttpStatus.OK);
        } catch (NoSuchEmployeeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
