package org.bahmni.insurance.dao;

import java.util.Date;
import java.util.List;

import org.bahmni.insurance.ImisConstants;
import org.bahmni.insurance.model.FhirResourceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class FhirResourceDaoServiceImpl implements IFhirResourceDaoService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<FhirResourceModel> findAll() {
		List<FhirResourceModel> result = jdbcTemplate.query("SELECT * FROM fhir_resource",
				(rs, rowNum) -> new FhirResourceModel(rs.getInt("id"), rs.getDate("ts"), rs.getString("version"),
						rs.getString("resource_type"), rs.getString("status"), rs.getString("resource")));

		return result;

	}
	
	
	
	@Override
	public int insertFhirResource(String fhirResource, String type) throws DataAccessException {
		return jdbcTemplate.update("INSERT INTO fhir_resource( ts, version, resource_type, status, resource) "
				+ "VALUES (?,?,?,?,CAST(? as jsonb))",  new Date(), ImisConstants.FHIR_VERSION, type, "Requested", fhirResource);
		

	}
	
	private  String convertStringToJson()  {
		
		String jsonStr = "{\r\n" + 
				"  \"resourceType\": \"Claim\",\r\n" + 
				"  \"id\": \"100150\",\r\n" + 
				"  \"text\": {\r\n" + 
				"    \"status\": \"generated\",\r\n" + 
				"    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">A human-readable rendering of the Oral Health Claim</div>\"\r\n" + 
				"  },\r\n" + 
				"  \"identifier\": [\r\n" + 
				"    {\r\n" + 
				"      \"system\": \"http://happyvalley.com/claim\",\r\n" + 
				"      \"value\": \"12345\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"status\": \"active\",\r\n" + 
				"  \"type\": {\r\n" + 
				"    \"coding\": [\r\n" + 
				"      {\r\n" + 
				"        \"system\": \"http://hl7.org/fhir/ex-claimtype\",\r\n" + 
				"        \"code\": \"oral\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"use\": \"complete\",\r\n" + 
				"  \"patient\": {\r\n" + 
				"    \"reference\": \"Patient/1\"\r\n" + 
				"  },\r\n" + 
				"  \"created\": \"2014-08-16\",\r\n" + 
				"  \"insurer\": {\r\n" + 
				"    \"reference\": \"Organization/2\"\r\n" + 
				"  },\r\n" + 
				"  \"organization\": {\r\n" + 
				"    \"reference\": \"Organization/1\"\r\n" + 
				"  },\r\n" + 
				"  \"priority\": {\r\n" + 
				"    \"coding\": [\r\n" + 
				"      {\r\n" + 
				"        \"code\": \"normal\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"payee\": {\r\n" + 
				"    \"type\": {\r\n" + 
				"      \"coding\": [\r\n" + 
				"        {\r\n" + 
				"          \"code\": \"provider\"\r\n" + 
				"        }\r\n" + 
				"      ]\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  \"careTeam\": [\r\n" + 
				"    {\r\n" + 
				"      \"sequence\": 1,\r\n" + 
				"      \"provider\": {\r\n" + 
				"        \"reference\": \"Practitioner/example\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"diagnosis\": [\r\n" + 
				"    {\r\n" + 
				"      \"sequence\": 1,\r\n" + 
				"      \"diagnosisCodeableConcept\": {\r\n" + 
				"        \"coding\": [\r\n" + 
				"          {\r\n" + 
				"            \"code\": \"123456\"\r\n" + 
				"          }\r\n" + 
				"        ]\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"insurance\": [\r\n" + 
				"    {\r\n" + 
				"      \"sequence\": 1,\r\n" + 
				"      \"focal\": true,\r\n" + 
				"      \"coverage\": {\r\n" + 
				"        \"reference\": \"Coverage/9876B1\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"item\": [\r\n" + 
				"    {\r\n" + 
				"      \"sequence\": 1,\r\n" + 
				"      \"careTeamLinkId\": [\r\n" + 
				"        1\r\n" + 
				"      ],\r\n" + 
				"      \"service\": {\r\n" + 
				"        \"coding\": [\r\n" + 
				"          {\r\n" + 
				"            \"code\": \"1200\"\r\n" + 
				"          }\r\n" + 
				"        ]\r\n" + 
				"      },\r\n" + 
				"      \"servicedDate\": \"2014-08-16\",\r\n" + 
				"      \"unitPrice\": {\r\n" + 
				"        \"value\": 135.57,\r\n" + 
				"        \"system\": \"urn:iso:std:iso:4217\",\r\n" + 
				"        \"code\": \"USD\"\r\n" + 
				"      },\r\n" + 
				"      \"net\": {\r\n" + 
				"        \"value\": 135.57,\r\n" + 
				"        \"system\": \"urn:iso:std:iso:4217\",\r\n" + 
				"        \"code\": \"USD\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		return jsonStr;
		
	}
	


	@Override
	public List<String> getClaimId() {
		List<String> result = jdbcTemplate.query("SELECT resource->'id' as id FROM fhir_resource",
				(rs, rowNum) -> rs.getString("id"));

		return result;
	}

}
