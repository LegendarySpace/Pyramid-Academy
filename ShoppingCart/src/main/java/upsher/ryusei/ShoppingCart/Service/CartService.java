package upsher.ryusei.ShoppingCart.Service;

import upsher.ryusei.ShoppingCart.Entity.Cart;
import upsher.ryusei.ShoppingCart.Entity.Product;

import java.util.Map;

public interface CartService {
    Map<Product, Integer> getCart();
    Product getProductInCartById(int productId);
    Product addProductToCart(Product product);  // TODO: return count instead
    Product addProductToCartById(int productId);
    boolean removeProductFromCartById(int productId);   // TODO: return count instead
    boolean addCart(Cart cart);
    boolean deleteCartById();
}
