package upsher.ryusei.ShoppingCart.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upsher.ryusei.ShoppingCart.DAO.CartDAO;
import upsher.ryusei.ShoppingCart.DAO.ProductDAO;
import upsher.ryusei.ShoppingCart.Entity.Cart;
import upsher.ryusei.ShoppingCart.Entity.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private ProductDAO productDAO;

    @Override
    public Map<Product, Integer> getCart() {
        var cart = cartDAO.findById(10);
        if (cart.isEmpty()) return new HashMap<>();
        return cart.get().getProducts();
    }

    @Override
    public Product getProductInCartById(int productId) {
        var cart = cartDAO.findById(10);
        if (cart.isEmpty()) return null;
        return cart.get().getProductById(productId).orElse(null);
    }

    @Override
    public Product addProductToCart(Product product) {
        var cart = cartDAO.findById(10).orElse(null);
        if (cart == null || product == null) return null;
        cart.addProduct(product);
        cartDAO.save(cart);
        return product;
    }

    @Override
    public Product addProductToCartById(int productId) {
        var product = productDAO.findById(productId).orElse(null);
        return addProductToCart(product);
    }

    @Override
    public boolean removeProductFromCartById(int productId) {
        var cart = cartDAO.findById(10).orElse(null);
        if (cart == null) return false;
        var ret = cart.removeProductById(productId);
        if (ret) cartDAO.save(cart);
        return ret;
    }

    @Override
    public boolean addCart(Cart cart) {
        cartDAO.save(cart);
        return true;
    }

    @Override
    public boolean deleteCartById() {
        cartDAO.deleteById(1);
        return true;
    }
}
