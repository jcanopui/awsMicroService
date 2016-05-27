package com.everis.aws.push.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="REGISTER_DEVICES")
public class RegisterEntity {
	public static final String TABLE_NAME = "REGISTER_DEVICES";
	public static final String FIELD_DEVICE_TOKEN = "deviceToken";
	public static final String FIELD_PLATFORM = "platform";
	public static final String FIELD_IDENTIFIER = "identifier";
	public static final String FIELD_ENDPOINT_ARN = "endpointARN";

	private String deviceToken;
	
	private String platform;
	
	private String identifier;

	private String endpointARN;

	public RegisterEntity(String deviceToken, String platform, String identifier, String endpointARN) {
		super();
		this.deviceToken = deviceToken;
		this.platform = platform;
		this.identifier = identifier;
		this.endpointARN = endpointARN;
	}
	
	// Needed for JSON libraries (instantation)
	public RegisterEntity() {
		super();
	}

	@DynamoDBHashKey()
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

	public String getEndpointARN() {
		return endpointARN;
	}

	public void setEndpointARN(String endpointARN) {
		this.endpointARN = endpointARN;
	}

	@Override
	public String toString() {
		return String.format("RegisterEntity [token=%s, platform=%s, identifier=%s, endpointARN=%s]",  deviceToken, platform, identifier, endpointARN);
	}
}
