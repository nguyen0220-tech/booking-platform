package com.catholic.ac.kr.booking_platform.helper.response;
 /*
    GraphQL Query
  */

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
