package upsher.ryusei;

public class Phone {

    private String mob;

    public Phone() {
    }

    public Phone(String mob) {
        this.mob = mob;
    }

    @Override
    public String toString() {
        return mob;
    }
}
