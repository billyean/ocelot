# *Organization Manager* (Intuit QBO interview questions)

**Organization Manager** is a spring boot application that let people to create a company structure 

Time spent: **8** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] Adding an employee to the Organization
* [X] Move the team. An employee can move to another team  within the organisation. When moving to a different team, an employee starts reporting to a new manager, without transferring his past subordinates to the new team. Instead, the most senior (based on start date) of his subordinates should be promoted to manage the employee’s former team.
* [X] Employee goes on holiday and gies back. 
  * [X] When an employee goes on holidays, all his subordinates start reporting to the employee's manager temporarily. 
  * [X] When an employee comes back from holidays, all his subordinates come back to report to him, unless they have moved teams.
* [X] Promotion.
  * [X] When an employee is promoted, he effectively becomes a peer of his former manager.
  * [X] attempting a promotion of one of the CEO’s subordinates will fail. 
  * [X] An employee can become a Director if he has at least 20 employees in his organisation (people subordinate to him, and his subordinates), including at least 2 managers (people that also have direct reports). 
  * [X] An employee can become a Vice President if he has at least 40 employees in his organisation (people subordinate to him, and his subordinates), including at least 4 directors. 


## Design
* [X] Spring boot application with embedded h2 database.
* [X] Using Spring JPA to populate the database. Two JPA repository interface service the purpose of populate the database
* [X] Two model classes Employee, Team serves the organization structure. Here is using composition design pattern. One Employee only belongs to one team, one team could have many employees
* [X] JSR310 supported date added. 
* [X] Enumeration role created for maintain the position.
* [X] One EmployeeService has all checking and logic.
* [X] Controller only works as restful API layer.

## Open-source libraries used

- [Spring Boot](https://projects.spring.io/spring-boot/) - Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".

## License

    Copyright [2017] [Haibo(Tristan) Yan]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
   

