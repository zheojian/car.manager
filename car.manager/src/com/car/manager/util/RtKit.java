package com.car.manager.util;

public class RtKit {
	private String status;
	private String info;
	
	public RtKit() {
		super();
	}

	public RtKit(String status, String info) {
		super();
		this.status = status;
		this.info = info;
	}

	public static RtKit rt(String status, String info) {
		return new RtKit(status, info);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public static RtKit rt(String status) {
		return new RtKit(status, "");
	}

	
}
