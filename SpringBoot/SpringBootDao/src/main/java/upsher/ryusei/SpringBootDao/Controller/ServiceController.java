package upsher.ryusei.SpringBootDao.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upsher.ryusei.SpringBootDao.Entity.Employee;
import upsher.ryusei.SpringBootDao.Service.EmployeeServices;

import java.util.List;

@RestController
public class ServiceController {

    @Autowired
    private EmployeeServices employeeServices;

    @GetMapping("/")
    public String home(@RequestParam(value="name", defaultValue = "World") String name,
                       @RequestParam(value = "msg", defaultValue = "Good Morning!") String msg){
        return String.format("<HTML><H1>Hello %s, %s</H1></HTML>",name,msg);
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return this.employeeServices.getAllEmployees();
    }

    @GetMapping("/employees/{employeeID}")
    public Employee getEmployee(@PathVariable String employeeID) {
        try {
            return this.employeeServices.getEmployeeFromID(Integer.parseInt(employeeID));
        } catch (NumberFormatException e) {
            System.out.println("Error parsing int from employeeID");
            return null;
        }
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee) {
        return this.employeeServices.addEmployee(employee);
    }

    @PutMapping("/employees")
    public boolean updateEmployee(@RequestBody Employee employee) {
        return this.employeeServices.updateEmployee(employee);
    }

    @DeleteMapping("/employees/{employeeID}")
    public boolean removeEmployeeByID(@PathVariable String employeeID) {
        try {
            return this.employeeServices.deleteEmployee(Integer.parseInt(employeeID));
        } catch (NumberFormatException e) {
            System.out.println("Error parsing int from employeeID");
            return false;
        }
    }


}
