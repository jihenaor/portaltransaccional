package com.serviciudad.portaltransaccional;


public class Amount{
    private String currency;
    private long total;
	
    public Amount(String currency, long total) {
		super();
		this.currency = currency;
		this.total = total;
	}
    
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
    
    
}
