package com.serviciudad.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClientResponse {
    private Status status;
    private int requestId;
    private String processUrl;
    private String actualizado;
}
