package com.serviciudad.security;

import com.google.gson.Gson;
import com.serviciudad.controller.ActualizarDiarioController;
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

	@Value("#{'${jms.jwt.excluded.rutasadministrador}'.split(',')}")
	private List<String> rutas_administrador;

	@Autowired
	private JwtIO jwtIO;

	static final Logger LOGGER = Logger.getLogger(String.valueOf(InterceptorJwtIO.class));

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean validate = false;
		String uri = request.getRequestURI();

		if (uri.contains(AUTH_PATH) || excluded(excluded, uri) || uri.contains(SWAGGER_PATH)) {
			validate = true;
		}

		if(!validate && request.getHeader("Authorization") != null && !request.getHeader("Authorization") .isEmpty()) {

			String token = request.getHeader("Authorization") .replace("Bearer ", "");

			UserModel userModel1 = new Gson().fromJson(jwtIO.getPayload(token), UserModel.class);

			if (userModel1.getPerfil().equals("ADMINISTRADOR") && !excluded(rutas_administrador, uri)) {
				validate = false;
			} else {
				validate = !jwtIO.validateToken(token);
			}
		}

		if(!validate) {
			LOGGER.info("No autorizado " + uri);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}

		return validate;
	}

	private boolean excluded(List<String> excludeds, String path) {

		boolean result = false;

		for(String exc : excludeds) {

			if(!exc.equals("#") && path.contains(exc)) {
				result = true;
			}
		}

		return result;
	}

}
