package upsher.ryusei;

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
        return String.format("%nCity: %s%nState: %s%nCountry: %s%nZipcode: %s",
                city, state, country, zipcode);
    }
}
