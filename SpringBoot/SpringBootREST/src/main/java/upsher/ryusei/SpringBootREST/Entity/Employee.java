package upsher.ryusei.SpringBootREST.Entity;

public class Employee {
    private int employeeID;
    private String name;
    private String email;

    public Employee() {
    }

    public Employee(int employeeID, String name, String email) {
        this.employeeID = employeeID;
        this.name = name;
        this.email = email;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
