package upsher.ryusei.ShoppingCart.Entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartId;

    @ManyToMany
    @JoinTable(name = "cart_product", joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @MapKey(name = "product_id")
    private final Map<Product, Integer> products;

    public Cart() {
        products = new HashMap<>();
    }

    public Cart(Set<Product> products) {
        this.products = new HashMap<>();
        for(var p : products) {
            this.products.put(p, 1);
        }
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        for(var p : products) {
            this.products.put(p, 1);
        }
    }

    public boolean addProduct(Product product) {
        if (products.containsKey(product)) products.put(product, products.get(product) + 1);
        else products.put(product, 1);
        return true;
    }

    public boolean removeProductById(int productId) {
        var product = products.keySet().stream().filter(p -> p.getProductId() == productId).findFirst().orElse(null);
        if (product == null) return false;
        return removeProduct(product);
    }

    public boolean removeProduct(Product product) {
        if (!products.containsKey(product)) return false;
        if (products.get(product) > 1) products.put(product, products.get(product) - 1);
        else products.remove(product);
        return true;
    }

    public Optional<Product> getProductById(int productId) {
        return products.keySet().stream().filter(p -> p.getProductId() == productId).findFirst();
    }
}
