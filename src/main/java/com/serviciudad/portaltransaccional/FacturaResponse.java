package com.serviciudad.portaltransaccional;

public class FacturaResponse {
    private Integer CodRespuesta;
    private String descripcion;
    private String idfactura;
    private String cuenta;
    private Integer tipofact;
    private Long totalfactura;
    private String fechapago;
	public Integer getCodRespuesta() {
		return CodRespuesta;
	}
	public void setCodRespuesta(Integer codRespuesta) {
		CodRespuesta = codRespuesta;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdfactura() {
		return idfactura;
	}
	public void setIdfactura(String idfactura) {
		this.idfactura = idfactura;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public Integer getTipofact() {
		return tipofact;
	}
	public void setTipofact(Integer tipofact) {
		this.tipofact = tipofact;
	}
	public Long getTotalfactura() {
		return totalfactura;
	}
	public void setTotalfactura(Long totalfactura) {
		this.totalfactura = totalfactura;
	}
	public String getFechapago() {
		return fechapago;
	}
	public void setFechapago(String fechapago) {
		this.fechapago = fechapago;
	}
    
    
}
