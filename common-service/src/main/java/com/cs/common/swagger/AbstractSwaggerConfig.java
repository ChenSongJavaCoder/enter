package com.cs.common.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @ClassName: SwaggerConfig
 * @Author: CS
 * @Date: 2019/11/1 15:15
 * @Description: TODO 暂不考虑使用统一的swagger
 */
public abstract class AbstractSwaggerConfig {
	/**
	 * 是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
	 */
	@Value(value = "${swagger.enabled}")
	Boolean swaggerEnabled;

	@Value(value = "${api.version}")
	String apiVersion;

	@Value(value = "${spring.application.name}")
	String title;

	@Value(value = "${swagger.owner.name}")
	String owner;

	@Value(value = "${swagger.owner.email}")
	String email;

	@Value(value = "${swagger.description}")
	String description;

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.enable(swaggerEnabled)
				.select()
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(title)
				.description(description)
				.termsOfServiceUrl("")
				.contact(new Contact(owner, "https://www.baidu.com", email))
				.version(apiVersion)
				.build();
	}

}
