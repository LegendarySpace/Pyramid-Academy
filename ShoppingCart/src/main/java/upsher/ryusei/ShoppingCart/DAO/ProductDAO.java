package upsher.ryusei.ShoppingCart.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upsher.ryusei.ShoppingCart.Entity.Apparel;
import upsher.ryusei.ShoppingCart.Entity.Book;
import upsher.ryusei.ShoppingCart.Entity.Product;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
    List<Apparel> findApparelBy();
}
