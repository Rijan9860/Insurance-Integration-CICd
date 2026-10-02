package org.bahmni.insurance.exception;

public class FhirFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FhirFormatException(String message) {
        super(message);
    }
}