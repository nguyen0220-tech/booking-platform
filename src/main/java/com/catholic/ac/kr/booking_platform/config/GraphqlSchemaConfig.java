package com.catholic.ac.kr.booking_platform.config;

import com.catholic.ac.kr.booking_platform.dto.MotelDTO;
import com.catholic.ac.kr.booking_platform.dto.RestaurantDTO;
import com.catholic.ac.kr.booking_platform.dto.SportDTO;
import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphqlSchemaConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .type("FacilityTarget", typeWiring -> typeWiring
                        .typeResolver(env -> {
                            Object src = env.getObject();
                            if (src instanceof SportDTO) {
                                return env.getSchema().getObjectType("Sport");
                            }
                            if (src instanceof MotelDTO) {
                                return env.getSchema().getObjectType("Motel");
                            }
                            if (src instanceof RestaurantDTO) {
                                return env.getSchema().getObjectType("Restaurant");
                            }
                            return null;
                        }));

    }
}
