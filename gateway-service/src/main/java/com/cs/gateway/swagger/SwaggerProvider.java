package com.cs.gateway.swagger;

import com.cs.gateway.config.IgnoreAuthUriConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SwaggerProvider
 * @Author: CS
 * @Date: 2019/10/31 14:26
 * @Description:
 */
@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {
    public static final String API_URI = "/v2/api-docs";

    public static final String EUREKA_SUB_PREFIX = "ReactiveCompositeDiscoveryClient_";


    @Autowired
    IgnoreAuthUriConfig ignoreAuthUriConfig;


    @Autowired
    private DiscoveryClientRouteDefinitionLocator routeLocator;

    @Override
    public List<SwaggerResource> get() {

        List<SwaggerResource> resources = new ArrayList<>();
        //从DiscoveryClientRouteDefinitionLocator 中取出routes，构造成swaggerResource
        routeLocator.getRouteDefinitions().subscribe(routeDefinition -> {
            if (ignoreAuthUriConfig.ignoreSwaggerUrlSuffix().stream().anyMatch(p -> StringUtils.endsWithIgnoreCase(routeDefinition.getId(), p))) {
                return;
            }
            resources.add(swaggerResource(routeDefinition.getId().substring(EUREKA_SUB_PREFIX.length()),
                    routeDefinition.getPredicates().get(0).getArgs().get("pattern").replace("/**", API_URI)));
        });

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");

        return swaggerResource;

    }
}