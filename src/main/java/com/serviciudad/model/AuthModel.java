package com.serviciudad.model;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="auth")
@NoArgsConstructor
@Getter
@Setter
public final class AuthModel {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String reference;
    private String descripcion;
    private long total;

	public AuthModel(String reference, String descripcion, long total) {
		this.id = UUID.randomUUID().toString();
		this.reference = reference;
		this.descripcion = descripcion;
		this.total = total;
	}
}
