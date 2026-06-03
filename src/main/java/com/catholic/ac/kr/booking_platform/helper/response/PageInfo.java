package com.catholic.ac.kr.booking_platform.helper.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PageInfo {
    private int page;
    private int size;
    private boolean hasNext;
    private long totalElements;
    private int totalPages;

    public PageInfo(int page, int size, boolean hasNext) {
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
    }
}