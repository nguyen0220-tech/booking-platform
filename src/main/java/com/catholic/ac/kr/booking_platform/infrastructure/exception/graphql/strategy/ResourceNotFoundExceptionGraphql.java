package com.catholic.ac.kr.booking_platform.infrastructure.exception.graphql.strategy;

import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class ResourceNotFoundExceptionGraphql implements ExceptionResolverStrategy<ResourceNotFoundException> {
    @Override
    public GraphQLError resolve(ResourceNotFoundException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.NOT_FOUND)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    @Override
    public Class<ResourceNotFoundException> getExceptionClass() {
        return ResourceNotFoundException.class;
    }
}
