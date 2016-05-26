package com.everis.aws.notifications.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="REGISTER_DEVICES")
public class RegisterEntity {
	public static final String FIELD_TOKEN = "token";
	public static final String FIELD_PLATFORM = "platform";
	public static final String FIELD_IDENTIFIER = "identifier";

	private String token;
	
	private String platform;
	
	private String identifier;

	public RegisterEntity(String token, String platform, String identifier) {
		super();
		this.token = token;
		this.platform = platform;
		this.identifier = identifier;
	}

	@DynamoDBHashKey()
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
