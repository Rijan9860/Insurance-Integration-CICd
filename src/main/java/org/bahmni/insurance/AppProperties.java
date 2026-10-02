package org.bahmni.insurance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProperties {
	
	@Value("${openmrs.db.url}")
	public String openmrsDBUrl;
	
	@Value("${openelis.db.url}")
	public String openelisDBUrl;
	
	@Value("${openmrs.root.url}")
	public String openmrsRootUrl;
	
	@Value("${imisconnect.user}")
	public String imisConnectUser;
	
	@Value("${imisconnect.password}")
	public String imisConnectPassword;
	
	@Value("${openimis.api.url}")
	public String imisUrl;
	
	@Value("${openimis.api.user}")
	public String imisUser;
	
	@Value("${openimis.api.password}")
	public String imisPassword;
	
	@Value("${openmrs.fhir.url}")
	public String openmrsFhirUrl;
	
	@Value("${openmrs.password}")
	public String openmrsPassword;
	
	@Value("${openmrs.user}")
	public String openmrsUser;
	
	@Value("${log4j.config.file}")
	public String log4jConfigFile;
	
	@Value("${openmrs.odoo.api}")
	public String openmrsOdooApi;
	
	@Value("${openimis.enterer.id}")
	public String openImisEntererId;
	
	@Value("${openimis.healthFacility.id}")
	public String openImisHFCode;
	
	@Value("${openimis.fhir.api.claim}")
	public String openImisFhirApiClaim;
	
	@Value("${imisconnect.eligresource.save}")
	public boolean saveEligResource;
	
	@Value("${imisconnect.claimresource.save}")
	public boolean saveClaimResource;
	
	@Value("${openimis.fhir.api.elig}")
	public String openImisFhirApiElig;
	
	@Value("${openimis.fhir.api.elig.policy.enabled}")
	public String openImisFhirApiEligPolicyEnabled;
	
	@Value("${openimis.policy.enabled}")
	public boolean openImisPolicyEnabled;
	
	@Value("${openimis.api.remote.user}")
	public String openImisRemoteUser;
	
	@Value("${openimis.fhir.api.refund}")
	public String openImisFhirApiRefund;
}
