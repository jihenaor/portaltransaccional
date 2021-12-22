package com.serviciudad.portaltransaccional;

public class Status{
    private String status;
    private String reason;
    private String message;
    private String date;
	public Status(String status, String reason, String message, String date) {
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
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
