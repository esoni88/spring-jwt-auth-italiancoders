package it.italiancoders.jwt.dao;

import it.italiancoders.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}