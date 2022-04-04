package upsher.ryusei;

import org.springframework.stereotype.Component;

@Component
public class Address {
    private String city;
    private String state;
    private String country;
    private String zipcode;

    public Address(String city, String state, String country, String zipcode) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }

    public Address() {
    }

    @Override
    public String toString() {
        return String.format("%n  City: %s%n  State: %s%n  Country: %s%n  Zipcode: %s",
                city, state, country, zipcode);
    }
}
