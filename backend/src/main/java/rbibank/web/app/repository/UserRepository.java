package rbibank.web.app.repository;

import rbibank.web.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByCustomerId(String customerId);

    List<User> findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrPanNumberContainingIgnoreCase(
        String firstname,
        String lastname,
        String panNumber
    );
}
