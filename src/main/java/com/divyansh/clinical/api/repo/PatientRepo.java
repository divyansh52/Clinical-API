package com.divyansh.clinical.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.divyansh.clinical.api.model.Patient;

public interface PatientRepo extends JpaRepository<Patient, Integer> {

}
