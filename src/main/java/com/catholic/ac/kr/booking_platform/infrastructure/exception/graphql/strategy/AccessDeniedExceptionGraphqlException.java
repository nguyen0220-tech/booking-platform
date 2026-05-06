package com.catholic.ac.kr.booking_platform.infrastructure.exception.graphql.strategy;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedExceptionGraphqlException implements ExceptionResolverStrategy<AccessDeniedException> {
    @Override
    public GraphQLError resolve(AccessDeniedException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.FORBIDDEN)
                .message("접속 권한이 없습니다." + ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }

    @Override
    public Class<AccessDeniedException> getExceptionClass() {
        return AccessDeniedException.class;
    }

}
