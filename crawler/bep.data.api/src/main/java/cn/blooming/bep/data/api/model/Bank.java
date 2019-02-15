package cn.blooming.bep.data.api.model;

import java.io.Serializable;

public class Bank implements Serializable {

	private static final long serialVersionUID = 1399094003133470534L;

	private String code;
	
	private String name;
	
	private String logoUrl;

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
