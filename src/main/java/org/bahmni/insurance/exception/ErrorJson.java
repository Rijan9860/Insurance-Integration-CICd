package org.bahmni.insurance.exception;

import java.util.Map;

public class ErrorJson {

	private Integer status;
	private String error;
	private String message;
	private String timeStamp;
	private String operationOutComeException;
	private String trace;

	public ErrorJson(int status, String operationOutComeException, Map<String, Object> errorAttributes) {
		this.status = status;
		this.error = (String) errorAttributes.get("error");
		this.message = (String) errorAttributes.get("message");
		this.timeStamp = errorAttributes.get("timestamp").toString();
		this.operationOutComeException = operationOutComeException;
		this.trace = (String) errorAttributes.get("trace");
	}

	public Integer getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getTrace() {
		return trace;
	}

	public String getOperationOutComeException() {
		return operationOutComeException;
	}

	public void setOperationOutComeException(String operationOutComeException) {
		this.operationOutComeException = operationOutComeException;
	}
}