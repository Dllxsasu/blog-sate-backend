package com.jeremias.dev.exception;

import java.util.Map;


import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;



import reactor.core.publisher.Mono;

@Component
@Import(DefaultServerCodecConfigurer.class)
@Order(-1)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
	public GlobalErrorWebExceptionHandler(final GlobalErrorAttributes globalErrorAttributes,
			final ApplicationContext applicationContext, final ServerCodecConfigurer serverCodecConfigurer) {
		super(globalErrorAttributes, new WebProperties.Resources(), applicationContext);
		super.setMessageReaders(serverCodecConfigurer.getReaders());
		super.setMessageWriters(serverCodecConfigurer.getWriters());
	}
	
	@Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest serverRequest) {
        final Map<String, Object> errorPropertiesMap = getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());

        return ServerResponse.status(HttpStatus.valueOf((Integer)errorPropertiesMap.get("status")))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
