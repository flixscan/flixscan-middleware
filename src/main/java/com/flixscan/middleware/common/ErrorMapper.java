package com.flixscan.middleware.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.mutiny.CompositeException;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class ErrorMapper implements ExceptionMapper<Exception> {
    private static final Logger LOGGER = Logger.getLogger(ErrorMapper.class.getName());
    @Inject
    ObjectMapper objectMapper;

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.error("Failed to handle request", exception);
        Throwable throwable = exception;
        int code = 500;
        if (throwable instanceof WebApplicationException) {
            code = ((WebApplicationException) exception).getResponse().getStatus();
        }
        if (throwable instanceof CompositeException) {
            throwable = ((CompositeException) throwable).getCause();
        }
        ObjectNode exceptionJson = objectMapper.createObjectNode();
        exceptionJson.put("exceptionType", throwable.getClass().getName());
        exceptionJson.put("code", code);

        if (exception.getMessage() != null) {
            exceptionJson.put("error", throwable.getMessage());
        }
        return Response.status(code)
                .entity(exceptionJson)
                .build();
    }
}
