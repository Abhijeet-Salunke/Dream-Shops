package com.shopingcart.dream_shops.repository;

import com.shopingcart.dream_shops.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findUserByEmail(String email);

    User findByEmail(String email);
}
