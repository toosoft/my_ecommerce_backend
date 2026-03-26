package com.ndiamond.paintshop.data;

import com.ndiamond.paintshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
//    Map<Object, Object> findByName(String role);
        Optional<Role> findByName(String role);
}
