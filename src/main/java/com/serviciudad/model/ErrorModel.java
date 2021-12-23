package com.serviciudad.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

@Entity
@Table(name="errores")
@NoArgsConstructor
@Getter
@Setter
public final class ErrorModel {
    @Id
    @Column(unique = true, nullable = false)
    private String id;
    private String msg;
    private String error;

    public ErrorModel(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        this.id = UUID.randomUUID().toString();
        this.msg = e.getMessage();
        this.error = sw.toString();
    }
}