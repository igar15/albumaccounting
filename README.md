[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e99d6883f16949cf967a1f52b341580a)](https://www.codacy.com/gh/ishlyakhtenkov/albumaccounting/dashboard)
[![Build Status](https://api.travis-ci.com/ishlyakhtenkov/albumaccounting.svg?branch=master)](https://travis-ci.com//ishlyakhtenkov/albumaccounting)

Album Accounting System project 
=================================

This is the REST API implementation of Album Accounting System designed for accounting constructor documentation albums, stored in the archive, and for tracking which employee has the constructor documentation album at the moment.

### Technology stack used: 
* Maven
* Spring Boot
* Spring MVC
* Spring Data JPA (Hibernate)
* Spring Security
* Ehcache
* REST (Jackson)
* JSON Web Token
* JUnit 5
* OpenAPI 3

### Project key logic:
* System main purpose: accounting constructor documentation albums, stored in the archive; obtaining information about the location of a specific album of constructor documentation at the moment.
* There are 3 types of users: admin, archive worker and anonymous users.
* Admins can create/update/delete users, departments, employees and albums.
* Archive workers can create/update/delete employees and albums. Also they can change their profile password.
* Anonymous users can only view information about albums.
* An archive worker, when issuing an album to the employee, enters the relevant information into the system.
* An archive worker, when the employee returns the album to the archive, enters the relevant information into the system.

### Application Domain Model Schema
![domain_model](https://user-images.githubusercontent.com/60218699/123831807-5d6a9380-d90d-11eb-93d5-a463fb4cf4fd.png)

### API documentation:
#### Swagger documentation
- (/v3/api-docs)
- (/swagger-ui.html)
#### Users
- GET /api/users (get all users)
- GET /api/users/by?keyWord={keyWord} (get all users by keyWord (search by name and email contains the keyWord))
- GET /api/users/{userId} (get user with id = userId)
- POST /api/users (create a new user)
- PUT /api/users/{userId} (update user with id = userId)
- PATCH /api/users/{userId}?enabled={enabledValue} (change user's enabled status to enabledValue for user with id = userId)
- PATCH /api/users/{userId}/password?password={password} (change user's password to 'password' request param for user with id = userId)
- DELETE /api/users/{userId} (delete user with id = userId)
#### Profile
- GET /api/profile (get user profile)
- PATCH /api/profile/password?password={password} (change user profile password)
- POST /api/profile/login (login to application)
#### Departments
- POST /api/departments (create a new department)
- GET /api/departments (get list of departments)
- GET /api/departments/{departmentId} (get department with id = departmentId)
- PUT /api/departments/{departmentId} (update department with id = departmentId)
- DELETE /api/departments/{departmentId} (delete department with id = departmentId)
#### Employees
- POST /api/employees (create a new employee)
- GET /api/departments/{departmentId}/employees (get all employees for department with id = departmentId)
- GET /api/employees/{employeeId} (get employee with id = employeeId)
- PUT /api/employees/{employeeId} (update employee with id = employeeId)
- DELETE /api/employees/{employeeId} (delete employee with id = employeeId)
#### Albums
- POST /api/albums (create a new album)
- GET /api/albums (get page of albums)
- GET /api/albums/byDecimal?decimalNumber={decimalNumber} (get page of albums (search albums by decimalNumber contains))
- GET /api/albums/byHolder?holderName={holderName} (get page of albums (search albums by holder name contains))
- GET /api/albums/{albumId} (get album with id = albumId)
- PUT /api/albums/{albumId} (update album with id = albumId)
- DELETE /api/albums/{albumId} (delete album with id = albumId)

### Caching strategy
#### Spring caching (Ehcache provider):
- Get user (multiNonExpiryCache, cache key = {email}, evicts all entries, when create/update/delete/enable/change_password of any user)
- Get all departments (singleNonExpiryCache, evicts when create/update/delete any department)
- Get all employees for a department (multiNonExpiryCache, cache key = {departmentId}, evicts all entries, when create/update/delete any employee, and when the department deletes)  

#### Hibernate 2nd level cache:
- Employee entity (CacheConcurrencyStrategy: NONSTRICT_READ_WRITE)
