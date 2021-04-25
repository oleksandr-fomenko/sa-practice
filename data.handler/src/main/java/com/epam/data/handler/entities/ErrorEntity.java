package com.epam.data.handler.entities;

public class ErrorEntity {

	private String field;
	private String message;

	public ErrorEntity(String message) {
		this.message = message;
	}

	public ErrorEntity(String field, String message) {
		this.field = field;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
