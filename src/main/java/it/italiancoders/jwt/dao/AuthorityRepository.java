package it.italiancoders.jwt.dao;


import it.italiancoders.jwt.model.Authority;
import it.italiancoders.jwt.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(AuthorityName name);

}