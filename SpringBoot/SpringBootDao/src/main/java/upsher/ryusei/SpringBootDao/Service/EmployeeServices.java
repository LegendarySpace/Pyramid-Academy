package upsher.ryusei.SpringBootDao.Service;

import upsher.ryusei.SpringBootDao.Entity.Employee;

import java.util.List;

public interface EmployeeServices {
    List<Employee> getAllEmployees();
    Employee getEmployeeFromID(int employeeID);
    Employee addEmployee(Employee employee);
    boolean updateEmployee(Employee employee); // Should contain other information
    boolean deleteEmployee(int employeeID);
}
