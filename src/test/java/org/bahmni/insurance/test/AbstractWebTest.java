package org.bahmni.insurance.test;

import java.io.IOException;

import javax.sql.DataSource;

import org.bahmni.insurance.SpringBootConsoleApplication;
import org.bahmni.insurance.dao.FhirResourceDaoServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(SpringBootConsoleApplication.class)
@WebAppConfiguration
@ContextConfiguration(classes = SpringBootConsoleApplication.class)
public abstract class AbstractWebTest {

	@MockBean
	private FhirResourceDaoServiceImpl fhirResourceDaoServiceImpl;

	@MockBean
	private DataSource datasource;

	protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	protected final IParser FhirParser = FhirContext.forDstu3().newJsonParser();

	public String mapToJson(Object obj) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	public <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}
}