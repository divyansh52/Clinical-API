package com.divyansh.clinical.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.divyansh.clinical.api.model.ClinicalData;
import com.divyansh.clinical.api.model.Patient;
import com.divyansh.clinical.api.repo.PatientRepo;
import com.divyansh.clinical.api.utility.BMICalculator;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {

	@Autowired
	private PatientRepo patientRepo;

	Map<String, String> filters = new HashMap<>();

	@GetMapping("/patients")
	public List<Patient> fetchAllPatient() {
		return patientRepo.findAll();
	}

	@GetMapping("/patients/{id}")
	public Patient fetchPatientById(@PathVariable("id") int id) {
		return patientRepo.findById(id).get();
	}

	@PostMapping("/patients")
	public Patient savePatient(@RequestBody Patient patient) {
		return patientRepo.save(patient);
	}

	@GetMapping("/patients/analyse/{id}")
	public Patient analyse(@PathVariable("id") int id) {
		Patient patient = patientRepo.findById(id).get();
		List<ClinicalData> clinicalData = patient.getClinicalData();

		List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
		for (ClinicalData data : duplicateClinicalData) {

			if (filters.containsKey(data.getComponentName())) {
				clinicalData.remove(data);
				continue;
			} else {
				filters.put(data.getComponentName(), null);
			}

			BMICalculator.calculateBMI(clinicalData, data);
		}
		filters.clear();
		return patient;
	}

	
	
	

}
