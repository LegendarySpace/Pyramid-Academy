package upsher.ryusei.ShoppingCart.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upsher.ryusei.ShoppingCart.DAO.ProductDAO;
import upsher.ryusei.ShoppingCart.Entity.Product;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDAO productDAO;

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public Product getProductById(int productId) {
        var product = productDAO.findById(productId).orElse(null);
        if (product == null) {
            System.out.println("Product ID not valid");
            return null;
        }
        return product;
    }

    @Override
    public Product addProduct(Product product) {
        productDAO.save(product);
        return product;
    }

    @Override
    public boolean updateProduct(Product product) {
        productDAO.save(product);
        return true;
    }

    @Override
    public boolean deleteProductById(int productId) {
        productDAO.deleteById(productId);
        return true;
    }
}
