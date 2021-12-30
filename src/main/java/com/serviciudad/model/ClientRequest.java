package com.serviciudad.model;

public class ClientRequest {
    private String locale;
    private Auth auth;
    private Payment payment;
    private String expiration;
    private String returnUrl;
    private String ipAddress;
    private String userAgent;
    
	public ClientRequest(String locale, Auth auth, Payment payment,
                         String expiration, String returnUrl, String ipAddress, String userAgent) {
		super();
		this.locale = locale;
		this.auth = auth;
		this.payment = payment;
		this.expiration = expiration;
		this.returnUrl = returnUrl;
		this.ipAddress = ipAddress;
		this.userAgent = userAgent;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public Auth getAuth() {
		return auth;
	}
	public void setAuth(Auth auth) {
		this.auth = auth;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
    
    
}
