package com.backend.backend_java.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PageResponse<T> {
    private int totalPage;
    private Long totalElement;
    private int currentPage;
    private int pageSize;
    private T data;
}
