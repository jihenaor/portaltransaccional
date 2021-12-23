package com.serviciudad;

public class Auth{
    private String login;
    private String tranKey;
    private String nonce;
    private String seed;

    
    public Auth(String login, String tranKey, String nonce, String seed) {
		super();
		this.login = login;
		this.tranKey = tranKey;
		this.nonce = nonce;
		this.seed = seed;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getTranKey() {
		return tranKey;
	}
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
    
    
}
