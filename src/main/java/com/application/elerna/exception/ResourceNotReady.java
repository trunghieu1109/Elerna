package com.application.elerna.exception;

import lombok.Getter;

@Getter
public class ResourceNotReady extends ResourceException {

    public ResourceNotReady(String resourceType, Long resourceId) {

        super(resourceType + " are not activated", resourceType, resourceId);
    }

}
