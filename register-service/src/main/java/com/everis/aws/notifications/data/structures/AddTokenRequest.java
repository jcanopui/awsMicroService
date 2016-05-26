package com.everis.aws.notifications.data.structures;

public class AddTokenRequest {

	private String platform;
	
	private String token;
	
	private String identifier;
	
	//values: "application", "email"
	private String protocol;
	
	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public AddTokenRequest(String platform, String token, String identifier) {
		this.platform = platform;
		this.token = token;
		this.identifier = identifier;
	}
	
	public AddTokenRequest() {
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
