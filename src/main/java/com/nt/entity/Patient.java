package com.nt.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nt.auth.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
@Table(name = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 40)
	private String name;

	private LocalDate birthDate;

	@Column(unique = true, nullable = false)
	private String email;

	private String gender;

	@OneToOne
	@MapsId
	private UserRole user;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "patient_insurance_id") 
	private Insurance insurance;

	@OneToMany(mappedBy = "patient", cascade = { CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Appointment> appointments = new ArrayList<>();
}