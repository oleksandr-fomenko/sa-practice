package com.epam.data.handler.exceptions;

public class EntityNotFoundException extends RuntimeException {

	public EntityNotFoundException(Object object) {
		super(String.format("Requested entity is not found in the storage:%n%s", object));
	}

}
