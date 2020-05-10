package bakalauras.demo.db;

import bakalauras.demo.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByPersonCode(String personCode);
}
