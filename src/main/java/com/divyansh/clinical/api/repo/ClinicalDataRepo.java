package com.divyansh.clinical.api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.divyansh.clinical.api.model.ClinicalData;

public interface ClinicalDataRepo extends JpaRepository<ClinicalData, Integer> {

	List<ClinicalData> findByPatientIdAndComponentNameOrderByMeasuredDateTime(int patientId, String componentName);

}
