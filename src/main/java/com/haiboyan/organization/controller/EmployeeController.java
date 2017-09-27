/*
 * Copyright [2017] [Haibo(Tristan) Yan]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @RequestMapping(method = RequestMethod.GET, value = "/{employeeId}")
    Employee getEmployee(@PathVariable Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{employeeId}/manager")
    Employee getEmployeeManager(@PathVariable Long employeeId) {
        return employeeService.getEmployeeManager(employeeId);
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
