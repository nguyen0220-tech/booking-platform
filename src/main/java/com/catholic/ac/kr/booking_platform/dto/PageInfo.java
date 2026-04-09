package com.catholic.ac.kr.booking_platform.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PageInfo {
    private int page;
    private int size;
    private boolean hasNext;
}