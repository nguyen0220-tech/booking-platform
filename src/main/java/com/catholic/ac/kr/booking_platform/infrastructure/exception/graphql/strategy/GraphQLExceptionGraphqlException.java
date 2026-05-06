package com.catholic.ac.kr.booking_platform.infrastructure.exception.graphql.strategy;

import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionGraphqlException implements ExceptionResolverStrategy<GraphQLException> {
    @Override
    public GraphQLError resolve(GraphQLException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    @Override
    public Class<GraphQLException> getExceptionClass() {
        return GraphQLException.class;
    }
}
