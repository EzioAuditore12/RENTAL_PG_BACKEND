package com.rental_pg_backend.configs;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class GlobalGraphqlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(
            @NonNull Throwable ex,
            @NonNull DataFetchingEnvironment env) {
                
        if (ex instanceof ResponseStatusException rse) {
            return GraphqlErrorBuilder.newError(env)
                    .message(rse.getReason() != null ? rse.getReason() : rse.getMessage())
                    .errorType(org.springframework.graphql.execution.ErrorType.BAD_REQUEST)
                    .build();
        }

        return null;
    }
}
