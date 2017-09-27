package com.haiboyan.organization.controller;

import com.haiboyan.organization.model.Team;
import com.haiboyan.organization.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addTeam(@RequestBody Team team) {
        try {
            employeeService.onCreateTeam(team);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> updateTeam(@RequestBody Team team) {
        try {
            employeeService.onCreateTeam(team);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
