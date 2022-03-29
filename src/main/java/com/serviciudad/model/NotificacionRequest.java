package com.serviciudad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificacionRequest {
    private Status status;
    private int requestId;
    private String reference;
    private String signature;
}
