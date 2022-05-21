package upsher.ryusei.ShoppingCart.Service;

import upsher.ryusei.ShoppingCart.Entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(int productId);
    Product addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProductById(int productId);
}
