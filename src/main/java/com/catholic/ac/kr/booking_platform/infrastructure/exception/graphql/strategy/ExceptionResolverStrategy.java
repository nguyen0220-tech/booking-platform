package com.catholic.ac.kr.booking_platform.infrastructure.exception.graphql.strategy;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

public interface ExceptionResolverStrategy<T extends Throwable> { //rằng buộc T
    GraphQLError resolve(T ex, DataFetchingEnvironment env);

    //hàm để lấy class của Exception
    Class<T> getExceptionClass();

    // Phương thức "cầu nối" để ép kiểu an toàn
    default GraphQLError handle(Throwable ex, DataFetchingEnvironment env) {
        if (getExceptionClass().isInstance(ex)) {
            // Class.cast(ex) sẽ trả về kiểu T, hoàn toàn hợp lệ với compiler
            return resolve(getExceptionClass().cast(ex), env);
        }
        return null;
    }
}