package com.catholic.ac.kr.booking_platform.exception;


import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class GraphqlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
        if (ex instanceof AccessDeniedException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.FORBIDDEN)
                    .message("접속 권한이 없습니다.")
                    .path(environment.getExecutionStepInfo().getPath())
                    .build();
        }


        if (ex instanceof GraphQLException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .path(environment.getExecutionStepInfo().getPath())
                    .location(environment.getField().getSourceLocation())
                    .build();
        }

        if (ex instanceof ConstraintViolationException cve){
            String message = cve.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath()+":"+v.getMessage())
                    .findFirst()
                    .orElse("Validation Error");

            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(message)
                    .path(environment.getExecutionStepInfo().getPath())
                    .build();
        }

        return null;
    }
}