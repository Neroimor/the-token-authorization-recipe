package com.example.Tokk.Repository;

import com.example.Tokk.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersJpaRepository extends JpaRepository<Users, Long> {

    public Optional<Users> findByEmail(String email);
}
