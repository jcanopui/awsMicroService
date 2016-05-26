package com.everis.aws.notifications.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="REGISTER_DEVICES")
public class RegisterEntity {
	public static final String TABLE_NAME = "REGISTER_DEVICES";
	public static final String FIELD_DEVICE_TOKEN = "device_token";
	public static final String FIELD_PLATFORM = "platform";
	public static final String FIELD_IDENTIFIER = "identifier";

	private String deviceToken;
	
	private String platform;
	
	private String identifier;

	public RegisterEntity(String deviceToken, String platform, String identifier) {
		super();
		this.deviceToken = deviceToken;
		this.platform = platform;
		this.identifier = identifier;
	}

	@DynamoDBHashKey()
	@DynamoDBAttribute(attributeName="device_token")
	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
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
