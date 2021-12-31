package com.serviciudad.modelpago;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class Status{
    private String status;
    private String reason;
    private String message;
    private String date;
}
