package upsher.ryusei.ShoppingCart.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upsher.ryusei.ShoppingCart.Entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
}
