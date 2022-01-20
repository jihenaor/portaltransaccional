package com.serviciudad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Auth{
    private String login;
    private String tranKey;
    private String nonce;
    private String seed;
}
