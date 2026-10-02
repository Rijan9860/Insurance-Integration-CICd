package org.bahmni.insurance.auth;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.log4j.Logger.getLogger;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	private final Logger logger = getLogger(AuthenticationEntryPoint.class);


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

		PrintWriter writer = response.getWriter();
		logger.debug("HTTP Status 401 : " + authException.getMessage());
		writer.println("HTTP Status 401 : " + authException.getMessage());

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setRealmName("Bahmni-OpenImis");
		super.afterPropertiesSet();
	}
}
