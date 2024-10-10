package com.application.elerna.exception;

public class ResourceNotFound extends ResourceException {

  public ResourceNotFound(String message) {
    super(message);
  }

  public ResourceNotFound(String resourceType, String feature) {
    super(resourceType + " not found", resourceType, feature);

  }
}
