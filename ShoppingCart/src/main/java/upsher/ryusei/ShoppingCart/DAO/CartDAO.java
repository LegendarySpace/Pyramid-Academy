package upsher.ryusei.ShoppingCart.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upsher.ryusei.ShoppingCart.Entity.Cart;
import upsher.ryusei.ShoppingCart.Entity.Product;

import java.util.Set;

@Repository
public interface CartDAO extends JpaRepository<Cart, Integer> {
}
