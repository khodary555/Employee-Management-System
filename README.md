# Employee Management System  

## members

- Ahmed Ibrahim El-sayed 11211
- Mohamed Ibrahim El-khodary 9981  

## Login credentials

username: admin  
password: admin123  

## file format used

we used CSV to store all credentials located in the home directory of this repository

## system description

A Java desktop application for managing a company's employees and departments.
Users log in, then add, view, search, update and delete employee records, and
manage the list of departments.

### Employee types

- **Full-time:** monthly salary is the basic salary plus a bonus.
- **Part-time:** monthly salary is the hourly rate times the hours worked.

Both extend an abstract `Employee` class (id, name, age, department, position),
and each one calculates its own salary.

### Main components

- **`Employee`, `FullTimeEmployee`, `PartTimeEmployee`:** the employee model.
- **`Department`:** an id and a name. The system starts with five defaults
  (IT, Human Resources, Finance, Marketing, Sales).
- **`EmployeeService`:** the core logic. It holds the employee and department
  lists, rejects duplicate ids and duplicate department names, and supports
  search by department.
- **`FileHandler`:** saves and loads the data in `employees.csv` and
  `departments.csv`, so records are kept between runs.
- **GUI:** a Swing interface with a login screen, a dashboard, and panels for
  adding, viewing, searching/updating and deleting employees and for managing
  departments. It only calls `EmployeeService`.

### How it works

On startup the service loads the data from the CSV files. Every action in the
interface goes through `EmployeeService`, and the data is saved back to the
files through `FileHandler`.  

## how to compile and run

clone this repository and enter the directory then from the terminal run `java MainFrame`  