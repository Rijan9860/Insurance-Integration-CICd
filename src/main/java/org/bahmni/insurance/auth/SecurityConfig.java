package org.bahmni.insurance.auth;

import org.bahmni.insurance.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@ComponentScan("org.bahmni.insurance")

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	private AppProperties properties;
	
	@Autowired
	public SecurityConfig(AppProperties properties) {
		this.properties = properties;
	}

	/*@Autowired
	private AuthenticationFilter authenticationFilter;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(authenticationFilter);
	}*/
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/h2-console/**")
		.permitAll()
		.antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
		.permitAll()
		.anyRequest().authenticated().and()
		.httpBasic()
		.authenticationEntryPoint(authEntryPoint);
		
        http.headers().frameOptions().disable();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.inMemoryAuthentication()
		.withUser(properties.imisConnectUser)
		.password("{noop}"+properties.imisConnectPassword)
		.roles("USER");
	}
	@Bean
	public HttpFirewall defaultHttpFirewall() {
	    return new DefaultHttpFirewall();
	}
	
	
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("swagger-ui.html")
	                .addResourceLocations("classpath:/META-INF/resources/");

	        registry.addResourceHandler("/webjars/**")
	                .addResourceLocations("classpath:/META-INF/resources/webjars/");
	    }
	


}
