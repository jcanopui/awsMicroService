package com.zurich;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.model.NotFoundException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.zurich.entities.RegisterEntity;
import com.zurich.entities.ResponseClass;

public class Register {
	private static final String ATTR_TOKEN = "Token";
	private static final String ATTR_ENABLED = "Enabled";
	private static final String TRUE = "true";
	private static final String TOPIC_ARN = "arn:aws:sns:us-east-1:688943189407:AMEU8";

	static AmazonSNS client = new AmazonSNSClient();

	static AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);

	public static ResponseClass registerToken(String token, String platform, String identifier) {

		boolean updateNeeded = false;
		boolean createNeeded = false;

		String platformArn = getPlatformArn(platform);

		String endpointArn = createEndpointWithPlatformAndToken(platformArn, token);

		try {
			GetEndpointAttributesRequest geaReq = new GetEndpointAttributesRequest().withEndpointArn(endpointArn);

			GetEndpointAttributesResult geaRes = client.getEndpointAttributes(geaReq);

			updateNeeded = !geaRes.getAttributes().get(ATTR_TOKEN).equals(token)
					|| !geaRes.getAttributes().get(ATTR_ENABLED).equalsIgnoreCase(TRUE);
		} catch (NotFoundException nfe) {
			createNeeded = true;
		}

		if (createNeeded) {
			endpointArn = createEndpointWithPlatformAndToken(platformArn, token);
		}

		if (updateNeeded) {
			updateEndpoint(endpointArn, token);
		}

		String subscriptionArn = subscribeToTopic(endpointArn);

		saveToDynamoDB(token, platform, identifier);

		return new ResponseClass(subscriptionArn);
	}

	private static String getPlatformArn(String platformType) {
		String platformArn = null;
		if (platformType.equalsIgnoreCase("android")) {
			platformArn = "arn:aws:sns:us-east-1:688943189407:app/GCM/AMEU8_GCM";
		} else if (platformType.equalsIgnoreCase("iosProd")) {
			platformArn = "arn:aws:sns:us-east-1:688943189407:app/APNS/AMEU8_APNS_PROD";
		} else if (platformType.equalsIgnoreCase("iosDev")) {
			platformArn = "arn:aws:sns:us-east-1:688943189407:app/APNS_SANDBOX/AMEU8_APNS_DEV";
		}
		return platformArn;
	}

	private static String createEndpointWithPlatformAndToken(String platformArn, String token) {
		String endpointArn = null;
		CreatePlatformEndpointRequest cpeReq = new CreatePlatformEndpointRequest()
				.withPlatformApplicationArn(platformArn).withToken(token);
		CreatePlatformEndpointResult cpeRes = client.createPlatformEndpoint(cpeReq);
		endpointArn = cpeRes.getEndpointArn();
		return endpointArn;
	}

	/*
	 * Update Endpoint with this endpointArn and this token
	 */
	private static void updateEndpoint(String endpointArn, String token) {
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(ATTR_TOKEN, token);
		attributes.put(ATTR_ENABLED, TRUE);
		SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest().withEndpointArn(endpointArn)
				.withAttributes(attributes);
		client.setEndpointAttributes(saeReq);
	}

	/*
	 * Subscribe to topic with this endpointarn
	 */
	private static String subscribeToTopic(String endpointArn) {
		SubscribeRequest subscribeReq = new SubscribeRequest(TOPIC_ARN, "application", endpointArn);
		SubscribeResult subscribeRes = client.subscribe(subscribeReq);
		return subscribeRes.getSubscriptionArn();
	}

	private static void saveToDynamoDB(String token, String platform, String identifier) {

		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

		RegisterEntity register = new RegisterEntity(token, platform, identifier);
		mapper.save(register);
	}
}
