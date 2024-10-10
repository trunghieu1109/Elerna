package com.application.elerna.exception;

import lombok.Getter;

@Getter
public class ResourceException extends RuntimeException {

    private String resourceType;
    private Long resourceId;
    private String resourceFeature;

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, String resourceType, Long resourceId) {
        super(message);
        this.resourceType = resourceType;
        this.resourceId = resourceId;

    }

    public ResourceException(String message, String resourceType, String resourceFeature) {
        super(message);
        this.resourceType = resourceType;
        this.resourceFeature = resourceFeature;
    }

}
