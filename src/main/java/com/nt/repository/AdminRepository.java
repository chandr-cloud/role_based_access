package com.nt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nt.entity.Admins;

@Repository
public interface AdminRepository extends JpaRepository<Admins, Long> {

	@Query(value = """
			SELECT u.id AS user_id,
			         u.username,
			   ur.roles,
			  COALESCE(a.name, d.name, p.name) AS fullName
			  FROM users u
			  LEFT JOIN user_role_roles ur ON ur.user_role_id = u.id
			  LEFT JOIN admins a ON a.user_id = u.id
			  LEFT JOIN doctor d ON d.user_id = u.id
			  LEFT JOIN patient p ON p.user_id = u.id GROUP BY u.id, u.username,a.name, d.name, p.name,ur.roles """, nativeQuery = true)
	List<Object[]> findAllUsersWithRolesAndDetails();

}
