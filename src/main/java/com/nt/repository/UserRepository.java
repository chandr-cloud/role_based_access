package com.nt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nt.auth.UserRole;

@Repository
public interface UserRepository extends JpaRepository<UserRole, Long> {
	@Query("SELECT u FROM UserRole u WHERE u.username = :email")
	Optional<UserRole> findByEmail(@Param("email") String email);
}
