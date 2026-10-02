package org.bahmni.insurance.auth;

import org.bahmni.insurance.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component(value = "authenticationFilter")
public class AuthenticationFilter extends HandlerInterceptorAdapter {

	public static final String REPORTING_COOKIE_NAME = "reporting_session";
	private OpenMRSAuthenticator authenticator;
	private AppProperties properties;

	@Autowired
	public AuthenticationFilter(OpenMRSAuthenticator authenticator, AppProperties properties) {
		this.authenticator = authenticator;
		this.properties = properties;
	}

	public void setAuthicator(OpenMRSAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("preHandle1");
		if (handler == null) {
			System.out.println("handler == null");
			response.sendError(HttpServletResponse.SC_NOT_FOUND,
					"Reports application cannot handle url " + request.getRequestURI());
			return false;
		}

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			System.out.println("cookies == null");
			return redirectToLogin(request, response);
		}
		AuthenticationResponse authenticationResponse = AuthenticationResponse.NOT_AUTHENTICATED;

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(REPORTING_COOKIE_NAME)) {
				System.out.println(" preHandle2" + cookie.getName());
				authenticationResponse = authenticator.authenticate(cookie.getValue());
			}
		}

		switch (authenticationResponse) {
		case AUTHORIZED:
			return true;
		case UNAUTHORIZED:
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Privileges is required to access reports");
			return false;
		default:
			return redirectToLogin(request, response);
		}

		/*
		 * Basic Authentication interception AuthenticationResponse
		 * authenticationResponse = AuthenticationResponse.NOT_AUTHENTICATED;
		 * 
		 * if (request instanceof HttpServletRequest) { HttpServletRequest httpRequest =
		 * (HttpServletRequest) request; if (httpRequest.getRequestedSessionId() != null
		 * && !httpRequest.isRequestedSessionIdValid()) { HttpServletResponse
		 * httpResponse = (HttpServletResponse) response;
		 * httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
		 * "Session timed out"); }
		 * 
		 * String basicAuth = httpRequest.getHeader("Authorization"); if (basicAuth !=
		 * null) { // this is "Basic ${base64encode(username + ":" + password)}" try {
		 * basicAuth = basicAuth.substring(6); // remove the leading "Basic " String
		 * decoded = new String(Base64.decodeBase64(basicAuth),
		 * Charset.forName("UTF-8")); String[] userAndPass = decoded.split(":");
		 * if("testUser".equalsIgnoreCase(userAndPass[0])) { authenticationResponse =
		 * AuthenticationResponse.AUTHORIZED; System.out.println("Hurray"); } //
		 * Context.authenticate(userAndPass[0], userAndPass[1]); } catch (Exception ex)
		 * { // This filter never stops execution. If the user failed to //
		 * authenticate, that will be caught later. } } }
		 * 
		 * switch (authenticationResponse) { case AUTHORIZED: return true; case
		 * UNAUTHORIZED: response.sendError(HttpServletResponse.SC_FORBIDDEN,
		 * "Privileges is required to access reports"); return false; default: return
		 * redirectToLogin(request, response); }
		 */
	}

	public boolean redirectToLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		httpServletResponse.getWriter().write("Please login to continue");

		StringBuffer redirectUrl = new StringBuffer();
		redirectUrl.append(properties.openmrsFhirUrl); // TODO: replace with bahmni url
		char paramChar = '?';
		if (redirectUrl.toString().contains("?")) {
			paramChar = '&';
		}
		redirectUrl.append(paramChar).append("from=").append(URLEncoder
				.encode(httpServletRequest.getRequestURL() + "?" + httpServletRequest.getQueryString(), "UTF-8"));
		httpServletResponse.sendRedirect(redirectUrl.toString());
		return false;
	}
}
