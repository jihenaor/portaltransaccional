package com.serviciudad.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Status{
    private String status;
    private String reason;
    private String message;
    private String date;
}
