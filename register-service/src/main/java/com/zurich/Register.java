package com.zurich;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
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
import com.zurich.data.structures.AddTokenResponse;
import com.zurich.entities.RegisterEntity;

@Component
public class Register {
	private static final String ATTR_TOKEN = "Token";
	private static final String ATTR_ENABLED = "Enabled";
	private static final String TRUE = "true";
	//private static final String TOPIC_ARN_FOR_PUSHES = "arn:aws:sns:us-east-1:688943189407:AMEU8";
	private static final String TOPIC_ARN_FOR_PUSHES = "arn:aws:sns:us-east-1:721597765533:PUSH_TO_DEVICE";
	private static final String PLATFORM_ANDROID = "android";
	private static final String ANDROID_APPLICATION = "arn:aws:sns:us-east-1:721597765533:app/GCM/ANDROID_SAMPLE_APPL";
	//private static final String ANDROID_APPLICATION = "arn:aws:sns:us-east-1:688943189407:app/GCM/AMEU8_GCM";
	
	@Autowired
	private AWSCredentials aWSCredentials;

	AmazonSNS client = new AmazonSNSClient(aWSCredentials);

	AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);

	public AddTokenResponse registerToken(String token, String platform, String identifier) {

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
			updateEndpointAndToken(endpointArn, token);
		}

		String subscriptionArn = subscribeToTopic(endpointArn);

		saveToDynamoDB(token, platform, identifier);

		return new AddTokenResponse(subscriptionArn);
	}

	private String getPlatformArn(String platformType) {
		String platformArn = null;
		if (platformType.equalsIgnoreCase(PLATFORM_ANDROID)) {
			platformArn = ANDROID_APPLICATION;
		} else 
			throw new RuntimeException("Platform type not supported: " + platformType);

		return platformArn;
	}

	private String createEndpointWithPlatformAndToken(String platformArn, String token) {
		String endpointArn = null;
		CreatePlatformEndpointRequest cpeReq = new CreatePlatformEndpointRequest()
				.withPlatformApplicationArn(platformArn).withToken(token);
		CreatePlatformEndpointResult cpeRes = client.createPlatformEndpoint(cpeReq);
		endpointArn = cpeRes.getEndpointArn();
		return endpointArn;
	}

	private void updateEndpointAndToken(String endpointArn, String token) {
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(ATTR_TOKEN, token);
		attributes.put(ATTR_ENABLED, TRUE);
		SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest().withEndpointArn(endpointArn)
				.withAttributes(attributes);
		client.setEndpointAttributes(saeReq);
	}

	private String subscribeToTopic(String endpointArn) {
		SubscribeRequest subscribeReq = new SubscribeRequest(TOPIC_ARN_FOR_PUSHES, "application", endpointArn);
		SubscribeResult subscribeRes = client.subscribe(subscribeReq);
		return subscribeRes.getSubscriptionArn();
	}

	private void saveToDynamoDB(String token, String platform, String identifier) {

		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

		RegisterEntity register = new RegisterEntity(token, platform, identifier);
		mapper.save(register);
	}

	private String findTokenFromUser(String identifier) {

		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

		//RegisterEntity register = new RegisterEntity(token, platform, identifier);
		//mapper.save(register);
		return null;
	}
}
