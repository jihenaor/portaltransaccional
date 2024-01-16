package com.serviciudad.service;

import com.serviciudad.compartido.exceptions.ApiUnauthorized;
import com.serviciudad.entity.LoginUser;
import com.serviciudad.entity.PasswordUser;
import com.serviciudad.entity.UserModel;
import com.serviciudad.model.JwtResponse;
import com.serviciudad.model.UserResponse;
import com.serviciudad.repository.UserRepository;
import com.serviciudad.security.JwtIO;
import com.serviciudad.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private JwtIO jwtIO;

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private DateUtils dateUtil;

    @Value("${jms.jwt.token.expires-in}")
    private int EXPIRES_IN;

    public JwtResponse login(String usuario, String contrasena) throws ApiUnauthorized {

        Optional<UserResponse> userResponse = userService.findByLogin(new LoginUser(usuario),
                new PasswordUser(contrasena));

        if (userResponse.isPresent()) {
            System.out.println("Login OK");
            JwtResponse jwtResponse = JwtResponse.builder()
                    .tokenType("bearer")
                    .accessToken(jwtIO.generateToken(userResponse.get()))
                    .issuedAt(dateUtil.getDateMillis() + "")
                    .clientId(usuario)
                    .expiresIn(EXPIRES_IN)
                    .build();

            return jwtResponse;
        } else {
            System.out.println("Login err");
            throw new ApiUnauthorized("Usuario no valido");
        }
    }

    public String login2(String usuario, String contrasena) {
        Optional<UserModel> userModel = userRepository.findById(usuario);
    //    Optional<UserResponse> userResponse = userService.findByLogin(new LoginUser(usuario), new PasswordUser(contrasena));

        return userModel.isPresent()? "S" : "N";
    }

}
