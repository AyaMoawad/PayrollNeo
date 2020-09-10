package com.rest.errorhandling;

public class UniqunessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UniqunessException(String message, Throwable cause) {
		super(message, cause);
	}

	public UniqunessException(String message) {
		super(message);
		}

	public UniqunessException(Throwable cause) {
		super(cause);
	}

	
}
