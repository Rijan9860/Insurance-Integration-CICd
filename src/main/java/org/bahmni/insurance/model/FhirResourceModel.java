package org.bahmni.insurance.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fhir_resource")
public class FhirResourceModel {

	@Id
	private int id;

	public FhirResourceModel(int id, Date timeStamp, String version, String resource_type, String status,
			String resource) {
		super();
		this.id = id;
		this.timeStamp = timeStamp;
		this.version = version;
		this.resource_type = resource_type;
		this.status = status;
		this.resource = resource;
	}

	public FhirResourceModel(int id) {
		this.id = id;
		// TODO Auto-generated constructor stub
	}

	public FhirResourceModel(String version) {
		this.version = version;
	}

	private Date timeStamp;

	private String version;

	private String resource_type;

	private String status;

	private String resource;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

}
