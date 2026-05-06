package com.catholic.ac.kr.booking_platform.infrastructure.exception.graphql;

import com.catholic.ac.kr.booking_platform.infrastructure.exception.graphql.strategy.ExceptionResolverStrategy;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GraphqlExceptionHandler extends DataFetcherExceptionResolverAdapter {
    private final Map<Class<? extends Throwable>, ExceptionResolverStrategy<?>> strategyMap;
    public GraphqlExceptionHandler(List<ExceptionResolverStrategy<?>> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        ExceptionResolverStrategy::getExceptionClass,
                        strategy -> strategy
                ));
    }

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
        ExceptionResolverStrategy<?> strategy = strategyMap.get(ex.getClass());

        // Nếu không có exact match, tìm theo kiểu kế thừa (Inheritance)
        if (strategy == null) {
            strategy = strategyMap.values().stream()
                    .filter(s -> s.getExceptionClass().isInstance(ex))
                    .findFirst()
                    .orElse(null);
        }

        if (strategy != null) {
            return strategy.handle(ex, environment);
        }

        return null;
    }
}