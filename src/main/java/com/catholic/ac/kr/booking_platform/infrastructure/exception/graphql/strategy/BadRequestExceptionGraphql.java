package com.catholic.ac.kr.booking_platform.infrastructure.exception.graphql.strategy;

import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class BadRequestExceptionGraphql implements ExceptionResolverStrategy<BadRequestException> {
    @Override
    public GraphQLError resolve(BadRequestException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message("Invalidate." + ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }

    @Override
    public Class<BadRequestException> getExceptionClass() {
        return BadRequestException.class;
    }

}
