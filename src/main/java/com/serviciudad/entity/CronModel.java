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
@Table(name="cron")
@NoArgsConstructor
@Getter
@Setter
public final class CronModel {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String fecha;
    private int pendientes;

    public CronModel(String id, String fecha, int pendientes) {
        this.id = id;
        this.fecha = fecha;
        this.pendientes = pendientes;
    }
}
