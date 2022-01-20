package com.serviciudad.model;

public class Payment{
    private String reference;
    private String description;
    private Amount amount;
    private boolean allowPartial;    
    
	public Payment(String reference, String description, Amount amount, boolean allowPartial) {
		super();
		this.reference = reference;
		this.description = description;
		this.amount = amount;
		this.allowPartial = allowPartial;
	}
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	public boolean isAllowPartial() {
		return allowPartial;
	}
	public void setAllowPartial(boolean allowPartial) {
		this.allowPartial = allowPartial;
	}
}
