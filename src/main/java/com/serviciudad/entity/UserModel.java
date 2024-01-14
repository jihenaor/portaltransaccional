package com.serviciudad.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="user")
@NoArgsConstructor
@Getter
@Setter
public final class UserModel {
    @Id
    @Column(unique = true, nullable = false, length = 15 )
    private String login;

    @Column(nullable = false, length = 40)
    private String password;

    @Column(nullable = false, length = 2)
    private String banco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Perfil perfil;
    public enum Perfil {
        ADMINISTRADOR, TESORERIA, BANCO, TESTING
    }
}
