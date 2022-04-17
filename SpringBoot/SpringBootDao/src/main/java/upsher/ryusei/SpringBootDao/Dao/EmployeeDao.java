package upsher.ryusei.SpringBootDao.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upsher.ryusei.SpringBootDao.Entity.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Integer> {
}
