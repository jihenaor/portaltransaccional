package com.serviciudad.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(nullable = false, length = 15)
    private String perfil;
}
