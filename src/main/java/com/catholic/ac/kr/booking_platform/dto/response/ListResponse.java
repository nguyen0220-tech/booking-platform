package com.catholic.ac.kr.booking_platform.dto.response;
 /*
    GraphQL Query
  */

import com.catholic.ac.kr.booking_platform.dto.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResponse<T> {
    private List<T> data;
    private PageInfo pageInfo;

    public ListResponse(List<T> data, PageInfo pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }
}
