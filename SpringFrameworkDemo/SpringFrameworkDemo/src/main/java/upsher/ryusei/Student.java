package upsher.ryusei;

import java.util.List;

public class Student {

    public Student() {
    }

    @Override
    public String toString() {
        String s = String.join("\n", ph.stream().map(Object::toString).toList());
        return String.format("ID: %d%nName: %s%nNumbers:%n%s%nAddress: %s", id, name, s, add);
    }

    public Student(int id, String name, List<Phone> phones, Address address) {
        this.id = id;
        this.name = name;
        this.ph = phones;
        this.add = address;
    }

    private int id;
    private String name;
    private List<Phone> ph;
    private Address add;
}

