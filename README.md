[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ddae95adbea54bed8afcc56f40b906ff)](https://www.codacy.com/gh/igar15/album-accounting/dashboard)
[![Build Status](https://api.travis-ci.com/igar15/album-accounting.svg?branch=master)](https://travis-ci.com//igar15/album-accounting)

Album accounting system project 
=================================

This is the REST API implementation of constructor documentation album accounting system for tracking which developer has the constructor documentation album at the moment.

### Technology stack used: 
* Maven
* Spring Boot
* Spring MVC
* Spring Security
* Spring Data JPA (Hibernate)
* REST (Jackson)
* JUnit

### Project key logic:
* System main purpose: obtaining information about the location of a specific album of constructor documentation at the moment.
* There are 3 types of users: admin, archive worker and anonymous users.
* Admins can create/update/delete users, departments, developers and albums.
* Archive workers can create/update/delete developers and albums. Also they can change their password.
* Anonymous users can only view information about albums.
* An archive worker, when issuing an album to the developer, enters the relevant information into the system.
* An archive worker, when the developer returns the album to the archive, enters the relevant information into the system.

###Application Domain Model Schema
![domain_model](https://user-images.githubusercontent.com/60218699/120080245-9c17ee80-c0c0-11eb-97b7-e147de9c895c.png)

### API documentation:
#### Swagger documentation
- (/v2/api-docs)
- (/swagger-ui.html)
#### Users
- GET /rest/users (get all users)
- GET /rest/users/{userId} (get user with id = userId)
- POST /rest/users (create a new user)
- PUT /rest/users/{userId} (update user with id = userId)
- PATCH /rest/users/{userId}?enabled={enabledValue} (change user's enabled status to enabledValue for user with id = userId)
- DELETE /rest/users/{userId} (delete user with id = userId)

- GET /rest/profile (get user profile)
- PUT /rest/profile/password (update user profile password)
#### Departments
- POST /rest/departments (create a new department)
- GET /rest/departments (get list of departments)
- GET /rest/departments/{departmentId} (get department with id = departmentId)
- PUT /rest/departments/{departmentId} (update department with id = departmentId)
- DELETE /rest/departments/{departmentId} (delete department with id = departmentId)
#### Employees
- POST /rest/departments/{departmentId}/employees (create a new employee for department with id = departmentId)
- GET /rest/departments/{departmentId}/employees (get all employees for department with id = departmentId)
- GET /rest/employees/{employeeId} (get employee with id = employeeId)
- PUT /rest/employees/{employeeId} (update employee with id = employeeId)
- DELETE /rest/employees/{employeeId} (delete employee with id = employeeId)
#### Albums
- POST /rest/albums (create a new album)
- GET /rest/albums (get list of albums)
- GET /rest/albums/{albumId} (get album with id = albumId)
- PUT /rest/albums/{albumId} (update album with id = albumId)
- PATCH /rest/albums/{albumId}?employeeId={employeeId} (change album's handler to employee with id = employeeId)
- DELETE /rest/albums/{albumId} (delete album with id = albumId)

### Caching strategy

####TODO TODO TODO

Spring caching (Ehcache provider):
- Get all departments (singleNonExpiryCache, evicts when create/update/delete any department)
- Get all employees for a department (nonExpiryCache, cache key = {departmentId}, evicts by key, when create/update/delete an employee for the department, and when the department deletes)  

Hibernate second level cache:
- Department entity
- Employee entity