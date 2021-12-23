package com.serviciudad;

public class StatusError {
    private String status;
    private int reason;
    private String message;
    private String date;
	
    public StatusError(String status, int reason, String message, String date) {
		super();
		this.status = status;
		this.reason = reason;
		this.message = message;
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
    
    
}
