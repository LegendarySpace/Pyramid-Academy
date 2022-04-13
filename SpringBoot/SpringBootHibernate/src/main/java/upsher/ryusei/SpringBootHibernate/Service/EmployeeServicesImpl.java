package upsher.ryusei.SpringBootHibernate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upsher.ryusei.SpringBootHibernate.Dao.EmployeeDao;
import upsher.ryusei.SpringBootHibernate.Entity.Employee;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServicesImpl implements EmployeeServices{

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDao.findAll();
    }

    @Override
    public Employee getEmployeeFromID(int employeeID) {
        var e = employeeDao.findById(employeeID).orElse(null);
        if (e == null) throw new RuntimeException(" Employee not found for id :: " + employeeID);
        return e;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        employeeDao.save(employee);
        return employee;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        employeeDao.save(employee);
        return true;
    }

    @Override
    public boolean deleteEmployee(int employeeID) {
        employeeDao.deleteById(employeeID);
        return true;
    }
}
