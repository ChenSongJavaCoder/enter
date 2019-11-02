package com.cs.gateway.filter;

import com.cs.common.util.RSAUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @ClassName: JwtTokenFilter
 * @Author: CS
 * @Date: 2019/11/2 16:40
 * @Description:
 */
@Component
public class JwtTokenFilter implements GlobalFilter, Ordered {

	/**
	 *
	 */
	private String[] skipAuthUrls = {"/ljl-auth/oauth/token"};
	/**
	 * 需要从url中获取token
	 */
	private String[] urlToken = {"/ljl-server-chat/websocket"};

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
		System.out.println(url);
		//跳过不需要验证的路径
		if (null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(url)) {
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
			return returnAuthFail(exchange, "请登陆");
		} else {
			//有token
			try {
				//解密token
				Claims jwt = getTokenBody(token);

				ServerHttpRequest oldRequest = exchange.getRequest();
				URI uri = oldRequest.getURI();
				ServerHttpRequest newRequest = oldRequest.mutate().uri(uri).build();
				// 定义新的消息头
				HttpHeaders headers = new HttpHeaders();
				headers.putAll(exchange.getRequest().getHeaders());
				headers.remove("Authorization");
				headers.set("Authorization", jwt.toString());

				newRequest = new ServerHttpRequestDecorator(newRequest) {
					@Override
					public HttpHeaders getHeaders() {
						HttpHeaders httpHeaders = new HttpHeaders();
						httpHeaders.putAll(headers);
						return httpHeaders;
					}
				};

				return chain.filter(exchange.mutate().request(newRequest).build());
                /*System.out.println(jwt.toString());
                //RSA公钥验签
                String jwtData[] =  token.split("\\.");
                Boolean isSgin = RSAUtil.verify((jwtData[0]+"."+jwtData[1]).getBytes(),jwtData[2]);
                if(isSgin){
                    return chain.filter(exchange);
                }else{
                    return returnAuthFail(exchange,"token验签失败");
                }*/
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
		String resultData = "{\"status\":\"-1\",\"msg\":" + message + "}";
		byte[] bytes = resultData.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return exchange.getResponse().writeWith(Flux.just(buffer));
	}

	private static Claims getTokenBody(String token) {
		return Jwts.parser()
				.setSigningKey(RSAUtil.getPublicKey())
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public int getOrder() {
		return -201;
	}
}
