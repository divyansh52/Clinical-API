package com.divyansh.clinical.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.divyansh.clinical.api.dto.ClinicalDataRequest;
import com.divyansh.clinical.api.model.ClinicalData;
import com.divyansh.clinical.api.model.Patient;
import com.divyansh.clinical.api.repo.ClinicalDataRepo;
import com.divyansh.clinical.api.repo.PatientRepo;
import com.divyansh.clinical.api.utility.BMICalculator;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClinicalDataController {

	@Autowired
	private ClinicalDataRepo clinicalDataRepo;

	@Autowired
	private PatientRepo patientRepo;

	@PostMapping("/clinicalData")
	public ClinicalData saveClinicalData(@RequestBody ClinicalDataRequest clinicalDataRequest) {

		Patient patient = patientRepo.findById(clinicalDataRequest.getPatientId()).get();

		ClinicalData clinicalData = new ClinicalData();
		clinicalData.setComponentName(clinicalDataRequest.getComponentName());
		clinicalData.setComponentValue(clinicalDataRequest.getComponentValue());
		clinicalData.setPatient(patient);

		return clinicalDataRepo.save(clinicalData);
	}

	@GetMapping("/clinicals/{patientId}/{componentName}")
	public List<ClinicalData> getClinicalData(@PathVariable("patientId") int patientId,
			@PathVariable("componentName") String componentName) {
		if(componentName.equals("bmi")) {
			componentName = "hw";
		}
		
		List<ClinicalData> clinicalData = clinicalDataRepo
				.findByPatientIdAndComponentNameOrderByMeasuredDateTime(patientId, componentName);
		List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
		for (ClinicalData data : duplicateClinicalData) {
			BMICalculator.calculateBMI(clinicalData, data);
		
		}

		return clinicalData;
	}

}
