package com.serviciudad.security;

import com.google.gson.Gson;
import com.serviciudad.entity.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

@Component
public class InterceptorJwtIO implements HandlerInterceptor {

	@Value("${jms.jwt.token.auth.path}")
	private String AUTH_PATH;

	@Value("${jms.jwt.token.swagger.path}")
	private String SWAGGER_PATH;

	@Value("#{'${jms.jwt.excluded.path}'.split(',')}")
	private List<String> excluded;

	@Value("#{'${jms.jwt.included.rutasadministrador}'.split(',')}")
	private List<String> rutas_administrador;

	@Value("#{'${jms.jwt.included.rutasbanco}'.split(',')}")
	private List<String> rutas_banco;

	@Autowired
	private JwtIO jwtIO;

	static final Logger LOGGER = Logger.getLogger(String.valueOf(InterceptorJwtIO.class));

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean validate = false;
		String uri = request.getRequestURI();

		if (uri.contains(AUTH_PATH) || included(excluded, uri) || uri.contains(SWAGGER_PATH)) {
			validate = true;
		}

		if(!validate && request.getHeader("Authorization") != null && !request.getHeader("Authorization") .isEmpty()) {
			UserModel userModel1;

			String token = request.getHeader("Authorization") .replace("Bearer ", "");

			validate = !jwtIO.validateToken(token);

			if (!validate) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return validate;
			}
			try {
				userModel1 = new Gson().fromJson(jwtIO.getPayload(token), UserModel.class);
			} catch (io.fusionauth.jwt.InvalidJWTException invalidJWTException) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return validate;
			}

			switch (userModel1.getPerfil()) {
				case "ADMINISTRADOR":
					validate = included(rutas_administrador, uri);
					break;
				case "BANCO":
					validate = included(rutas_banco, uri);
					break;
			}
		}

		if(!validate) {
			LOGGER.info("No autorizado " + uri);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}

		return validate;
	}

	private boolean included(List<String> excludeds, String path) {

		boolean result = false;

		for(String exc : excludeds) {

			if(!exc.equals("#") && path.contains(exc)) {
				result = true;
				break;
			}
		}

		return result;
	}

}
