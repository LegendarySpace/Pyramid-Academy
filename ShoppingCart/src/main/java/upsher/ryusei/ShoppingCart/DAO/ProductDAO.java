package upsher.ryusei.ShoppingCart.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upsher.ryusei.ShoppingCart.Entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
}
