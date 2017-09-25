package com.haiboyan.organization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addEmployee() {

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{employeeId}/promote")
    ResponseEntity<?> promoteEmployee(@PathVariable String employeeId) {

    }


    @RequestMapping(method = RequestMethod.PUT, value = "/{employeeId}/team/{teamId}")
    ResponseEntity<?> switchEmployeeTeam(@PathVariable String employeeId, @PathVariable String teamId) {

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{employeeId}/onLeave")
    ResponseEntity<?> onLeave(@PathVariable String employeeId) {

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{employeeId}/comeBack")
    ResponseEntity<?> onBack(@PathVariable String employeeId) {

    }
}
