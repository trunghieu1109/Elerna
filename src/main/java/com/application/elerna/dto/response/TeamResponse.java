package com.application.elerna.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class TeamResponse implements Serializable {

    private String name;

    private Date createdAt;

    private Date updatedAt;

}
