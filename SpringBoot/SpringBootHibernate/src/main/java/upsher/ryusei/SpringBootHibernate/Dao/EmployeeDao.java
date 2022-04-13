package upsher.ryusei.SpringBootHibernate.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upsher.ryusei.SpringBootHibernate.Entity.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Integer> {
}
