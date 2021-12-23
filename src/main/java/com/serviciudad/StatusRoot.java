package com.serviciudad;

public class StatusRoot {
    private StatusError status;

	public StatusRoot(StatusError status) {
		super();
		this.status = status;
	}

	public StatusError getStatus() {
		return status;
	}

	public void setStatus(StatusError status) {
		this.status = status;
	}
    
    
}
