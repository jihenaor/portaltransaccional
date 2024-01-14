package com.serviciudad.model;

import com.serviciudad.entity.UserModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;

@RequiredArgsConstructor
@Getter
public class UserResponse {
    private final String login;
    private final String banco;
    private final String perfil;


    public static UserResponse fromAgragate(UserModel userModel) {
        return new UserResponse(userModel.getLogin(),
                                userModel.getBanco(),
                                userModel.getPerfil().toString());
    }
}
