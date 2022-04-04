package upsher.ryusei;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class Phone {
    //@Value("(615)8234972")
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
