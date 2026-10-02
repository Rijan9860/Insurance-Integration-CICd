package org.bahmni.insurance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ImisConstants {
   public static final int OPENIMIS_FHIR = 0;
   public static final int OPENMRS_FHIR = 1;
   public static final int OPENMRS_ODOO = 2;
   public static final int OPENMRS_API = 3;
   public static final Map<Integer, String> ERROR_CODE_TO_TEXT_MAP = Collections.unmodifiableMap(new HashMap<Integer, String>() {
      private static final long serialVersionUID = 1L;

      {
         this.put(-1, "rejected by medical officer");
         this.put(0, "accepted");
         this.put(1, "item service not in the register");
         this.put(2, "item service not in the pricelist");
         this.put(3, "item service not covered by policy");
         this.put(4, "item service doesnt comply with limitation");
         this.put(5, "item service doesnt comply with frequency");
         this.put(6, "item service duplicated");
         this.put(7, "item service not valid insurance number");
         this.put(8, "diagnosis code not in the currentlist");
         this.put(9, "target date of provision healthcare invalid");
         this.put(10, "item service doesnot comply with care constraint");
         this.put(11, "maximum number of inpatient exceeded");
         this.put(12, "maximum number of outpatient exceeded");
         this.put(13, "maximum number of consultations exceeded");
         this.put(14, "maximum number of surgeries exceeded");
         this.put(15, "maximum number of deliveries exceeded");
         this.put(16, "maximum number of provisions exceeded");
         this.put(17, "item service cannot be covered with waitingperiod");
         this.put(18, "na");
         this.put(19, "maximum number of antenetal exceeded");
      }
   });
   public static final String FHIR_VERSION = "STU3";
   public static final String FHIR_CLIENT = "fhir";
   public static final String REST_CLIENT = "rest";
   public static final String ADJUDICATION_ELIGIBLE = "eligible";
   public static final String ADJUDICATION_BENEFIT = "benefit";
   public static final String CLAIM_ID = "claimId";
   public static final String CLAIM_ITEMS = "item";
   public static final String ICD_10 = "ICD 10 - WHO";
   public static final String INSUREE_ID = "insureeId";
   public static final String PATIENT_UUID = "patientUUID";
   public static final String VISIT_UUID = "visitUUID";
   public static final String FHIR_CODE_FOR_IMIS_CLAIM_CODE_TYPE = "MR";
   public static final String FHIR_VALUESET_SYSTEM = "https://hl7.org/fhir/valueset-identifier-type.html";
   public static final String FHIR_PRODUCT = "http://hl7.org/fhir/StructureDefinition/Product";

   public static enum CLAIM_OUTCOME {
      REJECTED("rejected"),
      ENTERED("entered"),
      CHECKED("checked"),
      PROCESSED("processed"),
      VALUATED("valuated");

      private String outcome;

      private CLAIM_OUTCOME(String outcome) {
         this.outcome = outcome;
      }

      public String getOutCome() {
         return this.outcome;
      }
   }

   public static enum FHIR_RESOURCE_TYPE {
      CLAIM("CLAIM"),
      CLAIMRESPONSE("CLAIMRESPONSE"),
      ELIGIBILITYREQUEST("ELIGIBILITYREQUEST"),
      ELIGIBILITYRESPONSE("ELIGIBILITYRESPONSE"),
      CLAIMTRACK("CLAIMTRACK");

      private String value;

      private FHIR_RESOURCE_TYPE(String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }
   }

   public static class CLAIM_VISIT_TYPE {
      public static final String OPD = "OPD";
      public static final String IPD = "IPD";
      public static final String OPD_CODE = "O";
      public static final String OTHERS_CODE = "o";
      public static final String EMERGENCY_CODE = "E";
      public static final String REFFERALS_CODE = "R";
      public static final String OTHERS = "others";
      public static final String EMERGENCY = "Emergency";
      public static final String REFFERALS = "referrals";
   }

   public class CLAIM_ADJ_CATEGORY {
      public static final String GENERAL = "general";
      public static final String REJECTED_REASON = "rejected_reason";
   }

   public class CLAIM_ITEM_STATUS {
      public static final String PASSED = "passed";
      public static final String REJECTED = "rejected";
   }
}
