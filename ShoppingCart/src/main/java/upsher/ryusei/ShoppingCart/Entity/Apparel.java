package upsher.ryusei.ShoppingCart.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class Apparel extends Product{
    private String type;
    private String brand;
    private String designer;

    public Apparel() {
    }

    public Apparel(String name, double price, String type, String brand, String designer) {
        super(name, price);
        this.type = type;
        this.brand = brand;
        this.designer = designer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }
}
