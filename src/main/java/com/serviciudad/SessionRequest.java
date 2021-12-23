package com.serviciudad;

public class SessionRequest {
    private String reference;
    private String descripcion;
    private long total;
    
    public SessionRequest(String reference, String descripcion, long total) {
		super();
		this.reference = reference;
		this.descripcion = descripcion;
		this.total = total;
	}
	public String getReference() {
		return reference;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public long getTotal() {
		return total;
	}


}
