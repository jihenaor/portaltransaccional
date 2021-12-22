package com.serviciudad.portaltransaccional;



import javax.persistence.*;

@Entity
@Table(name="auth")
public final class AuthModel {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String reference;
    private String descripcion;
    private long total;
	
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
}
