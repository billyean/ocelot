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
package com.haiboyan.organization.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * An employee has to be assigned to a team when created or moved.
 */
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Team tempTeam;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    public Long getId() {
        return id;
    }

    public Employee() {
        state = State.IN_POSITION;
    }

    public Employee(String name, LocalDate hireDate, Role role) {
        this();
        this.name = name;
        this.hireDate = hireDate;
        this.role = role;
    }

    public boolean isPermanent() {
        return role != Role.Contractor;
    }

    public boolean isManager() {
        return role != Role.Contractor && role != Role.Permanent_Employee;
    }

    public boolean isInPostion() {
        return state == State.IN_POSITION;
    }

    public boolean isValid() {
        return state != State.QUITTED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTempTeam() {
        return tempTeam;
    }

    public void setTempTeam(Team tempTeam) {
        this.tempTeam = tempTeam;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
