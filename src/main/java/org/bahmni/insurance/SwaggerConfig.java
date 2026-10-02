package org.bahmni.insurance;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;


@Configuration
@EnableSwagger2
@EnableAutoConfiguration
@PropertySource({"classpath:application.properties"})
public class SwaggerConfig {
   @Bean
   public Docket apiDocket() {
      return (new Docket(DocumentationType.SWAGGER_2)).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
   }
}