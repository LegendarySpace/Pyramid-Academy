package upsher.ryusei.ShoppingCart.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upsher.ryusei.ShoppingCart.DAO.UserDAO;
import upsher.ryusei.ShoppingCart.Entity.*;
import upsher.ryusei.ShoppingCart.Service.CartService;
import upsher.ryusei.ShoppingCart.Service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class Controller {
    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/")
    public List<Product> setup() {
//        var b1 = new Book("Iliad", 29.99, "Mythology", "Homer", "Greeks");
//        var b2 = new Book("Odyssey", 29.99, "Mythology", "Homer", "Greeks");
//        var b3 = new Book("Aeneid", 29.99, "Mythology", "Virgil", "Romans");
//        var a1 = new Apparel("Basic Shirt", 24.99, "T-Shirt", "Target", "Who Cares");
//        var a2 = new Apparel("Fancy Shirt", 34.99, "Button-Up", "Macy's", "Nobody");
//        var a3 = new Apparel("Nice Suit", 109.99, "Suit", "Sak's 5th Ave", "Somebody");
//        productService.addProduct(b1);
//        productService.addProduct(b2);
//        productService.addProduct(b3);
//        productService.addProduct(a1);
//        productService.addProduct(a2);
//        productService.addProduct(a3);
        return productService.getAllProducts();
    }

    @GetMapping("/cart")
    public Map<Product, Integer> getCart() {
        return cartService.getCart();
    }

    @GetMapping("/cart/{productId}")
    public Product getCart(@PathVariable String productId) {
        try {
            return cartService.getProductInCartById(Integer.parseInt(productId));
        } catch (NumberFormatException e) {
            System.out.println("Not a valid Product ID");
            return null;
        }
    }

    @PutMapping("/cart")
    public Product addToCart(@RequestBody Product product) {
        return cartService.addProductToCart(product);
    }

    @PutMapping("/cart/add/{productId}")
    public Product addToCart(@PathVariable String productId) {
        try {
            return cartService.addProductToCartById(Integer.parseInt(productId));
        } catch (NumberFormatException e) {
            System.out.println("Not a valid Product ID");
            return null;
        }
    }

    @PutMapping("/cart/{productId}")
    public boolean removeFromCart(@PathVariable String productId) {
        try {
            return cartService.removeProductFromCartById(Integer.parseInt(productId));
        } catch (NumberFormatException e) {
            System.out.println("Not a valid Product ID");
            return false;
        }
    }

    @PostMapping("/cart")
    public boolean createCart(@RequestBody Cart cart) {
        return cartService.addCart(cart);
    }

    @DeleteMapping("/cart")
    public boolean deleteCart() {
        return cartService.deleteCartById();
    }

    @PostMapping("/user")
    public boolean createUser(@RequestBody Cart cart) {
        var u = new User(cart);
        userDAO.save(u);
        return true;
    }

    @DeleteMapping("/user")
    public boolean deleteUser() {
        userDAO.deleteById(11);
        return true;
    }
}
