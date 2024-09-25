package com.application.elerna.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class PageResponse<T> implements Serializable {

    private int status;
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalPages;
    private T data;

}
