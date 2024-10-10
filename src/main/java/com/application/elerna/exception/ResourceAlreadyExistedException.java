package com.application.elerna.exception;

public class ResourceAlreadyExistedException extends ResourceException {

    public ResourceAlreadyExistedException(String resourceType, Long resourceId) {

        super(resourceType + " has already existed in database", resourceType, resourceId);
    }
}
