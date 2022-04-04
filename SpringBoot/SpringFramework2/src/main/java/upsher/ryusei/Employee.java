package upsher.ryusei;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Employee {

    @Value("101")
    private int id;
    @Value("Carl")
    private String name;
    @Autowired
    private List<Phone> ph;
    @Autowired
    private Address add;

    public Employee() {
    }

    public Employee(int id, String name, List<Phone> phones, Address address) {
        this.id = id;
        this.name = name;
        this.ph = phones;
        this.add = address;
    }

    @Override
    public String toString() {
        String s = String.join("\n  ", ph.stream().map(Object::toString).toList());
        return String.format("ID: %d%nName: %s%nNumbers:%n      %s%nAddress: %s", id, name, s, add);
    }
}
