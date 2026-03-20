package com.nt.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nt.auth.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@MapsId
	private UserRole user;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(length = 100)
	private String specialization;

	@Column(unique = true, length = 100)
	private String email;

	@ManyToMany(mappedBy = "doctors")
	private Set<Department> departments = new HashSet<>();

	@OneToMany(mappedBy = "doctor")
	private List<Appointment> appointments = new ArrayList<>();

	private Boolean headDoctor;

}
