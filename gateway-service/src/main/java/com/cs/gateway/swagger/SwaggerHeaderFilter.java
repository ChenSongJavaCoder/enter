package com.cs.gateway.swagger;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @ClassName: SwaggerHeaderFilter
 * @Author: CS
 * @Date: 2019/10/31 14:26
 * @Description:
 */
@Component
public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {
	private static final String HEADER_NAME = "X-Forwarded-Prefix";

	@Override
	public GatewayFilter apply(Object config) {

		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			String path = request.getURI().getPath();
			if (!StringUtils.endsWithIgnoreCase(path, SwaggerProvider.API_URI)) {
				return chain.filter(exchange);
			}
			ServerHttpRequest newRequest = request.mutate().build();
			ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

			return chain.filter(newExchange);
		};

	}
}