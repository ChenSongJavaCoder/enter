package com.cs.gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.gateway.config.IgnoreAuthUriConfig;
import com.cs.gateway.swagger.SwaggerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;

/**
 * @ClassName: JwtTokenFilter
 * @Author: CS
 * @Date: 2019/11/2 16:40
 * @Description:
 */
@Slf4j
@Component
public class JwtTokenFilter implements GlobalFilter, Ordered {

	@Autowired
	IgnoreAuthUriConfig ignoreUriConfig;

	/**
	 * 需要从url中获取token
	 */
	private String[] urlToken = {"/chat/websocket"};

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 过滤器
	 *
	 * @param exchange
	 * @param chain
	 * @return
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String url = exchange.getRequest().getURI().getPath();
		log.info(url);
		//跳过不需要验证的路径
		if (ignoreUriConfig.ignoreUrls().contains(url)) {
			return chain.filter(exchange);
		}
		if (StringUtils.endsWithIgnoreCase(url, SwaggerProvider.API_URI)) {
			return chain.filter(exchange);
		}
		//获取token
		String token = exchange.getRequest().getHeaders().getFirst("Authorization");
		if (null != urlToken && Arrays.asList(urlToken).contains(url)) {
			//该方法需要修改
			String tokens[] = exchange.getRequest().getURI().getQuery().split("=");
			token = tokens[1];
		}
		if (StringUtils.isBlank(token)) {
			//没有token
			return returnAuthFail(exchange, "吼吼.要先登录的哦~");
		} else {
			//有token
			try {
				JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("123456ef"))
						.build(); //Reusable verifier instance
				DecodedJWT jwt = jwtVerifier.verify(token);
				if (jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
					throw new Exception("Token expires");
				}
				//TODO 可以进一步校验,识别用户身份信息
				log.info("jwt ---- {}", objectMapper.writeValueAsString(jwt));
				return chain.filter(exchange);
			} catch (ExpiredJwtException e) {
				e.printStackTrace();
				return returnAuthFail(exchange, "token超时");
			} catch (Exception e) {
				e.printStackTrace();
				return returnAuthFail(exchange, "token验签失败");
			}
		}
	}

	/**
	 * 返回校验失败
	 *
	 * @param exchange
	 * @return
	 */
	private Mono<Void> returnAuthFail(ServerWebExchange exchange, String message) {
		ServerHttpResponse serverHttpResponse = exchange.getResponse();
		serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		String resultData = message;
		byte[] bytes = resultData.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return exchange.getResponse().writeWith(Flux.just(buffer));
	}


	@Override
	public int getOrder() {
		return -201;
	}
}
