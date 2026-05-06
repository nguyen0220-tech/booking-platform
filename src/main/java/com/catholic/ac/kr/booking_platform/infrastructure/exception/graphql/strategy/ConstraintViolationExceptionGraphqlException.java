package com.catholic.ac.kr.booking_platform.infrastructure.exception.graphql.strategy;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class ConstraintViolationExceptionGraphqlException implements ExceptionResolverStrategy<ConstraintViolationException> {
    @Override
    public GraphQLError resolve(ConstraintViolationException ex, DataFetchingEnvironment env) {
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ":" + v.getMessage())
                .findFirst()
                .orElse("Validation Error");

        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }

    @Override
    public Class<ConstraintViolationException> getExceptionClass() {
        return ConstraintViolationException.class;
    }
}
