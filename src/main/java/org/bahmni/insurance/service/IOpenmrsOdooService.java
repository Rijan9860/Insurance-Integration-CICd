package org.bahmni.insurance.service;

import org.springframework.stereotype.Component;

@Component
public interface IOpenmrsOdooService {
	public String getOrderCost(String patientId);

}
