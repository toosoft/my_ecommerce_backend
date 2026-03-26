package com.ndiamond.paintshop.repository;

import com.ndiamond.paintshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

//    User getUserByOrderId(Long orderId);
}
