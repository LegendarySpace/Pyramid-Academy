package upsher.ryusei.SpringBootREST.Service;

import org.springframework.stereotype.Service;
import upsher.ryusei.SpringBootREST.Entity.Employee;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServicesImpl implements EmployeeServices{
    List<Employee> list;

    public EmployeeServicesImpl() {
        list = new ArrayList<>();
        list.add(new Employee(007, "James Bond", "James.Bond@Spy.com"));
        list.add(new Employee(23, "Michael Jordan", "Michael.Jordan@Jordan.com"));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return list;
    }

    @Override
    public Employee getEmployeeFromID(int employeeID) {
        return list.stream().filter(e -> e.getEmployeeID() == employeeID).findFirst().orElse(null);
    }

    @Override
    public Employee addEmployee(Employee employee) {
        list.add(employee);
        return employee;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        Employee emp = list.stream().filter(e -> e.getEmployeeID() == employee.getEmployeeID())
                .findFirst().orElse(null);
        if (emp == null) return false;
        emp.setName(employee.getName());
        emp.setEmail(employee.getEmail());
        return true;
    }

    @Override
    public boolean deleteEmployee(int employeeID) {
        list.stream().filter(e -> e.getEmployeeID() == employeeID).forEach(e -> list.remove(e));
        return true;
    }
}
