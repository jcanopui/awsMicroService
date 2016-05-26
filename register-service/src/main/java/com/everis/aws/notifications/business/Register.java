package com.everis.aws.notifications.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.apigateway.model.NotFoundException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.everis.aws.notifications.data.structures.AddTokenResponse;
import com.everis.aws.notifications.entities.RegisterEntity;

@Component
public class Register {
	private static final String ATTR_TOKEN = "Token";
	private static final String ATTR_ENABLED = "Enabled";
	private static final String TRUE = "true";
	private static final String TOPIC_ARN_FOR_PUSHES = "arn:aws:sns:us-east-1:721597765533:PUSH_TO_DEVICE";
	private static final String PLATFORM_ANDROID = "android";
	private static final String ANDROID_APPLICATION = "arn:aws:sns:us-east-1:721597765533:app/GCM/ANDROID_SAMPLE_APPL";

	@Autowired
	private AmazonSNS client;

	@Autowired
	private AmazonDynamoDB dynamoClient;

	public AddTokenResponse registerToken(String token, String platform, String identifier) {
		String subscriptionArn = registerNotificationDevice(platform, token);

		saveToDynamoDB(token, platform, identifier);

		return new AddTokenResponse(subscriptionArn);
	}

	private String registerNotificationDevice(String platform, String token) {
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

		return subscribeToTopic(endpointArn);
	}

	private String getPlatformArn(String platformType) {
		if (!platformType.equalsIgnoreCase(PLATFORM_ANDROID))
			throw new RuntimeException("Platform type not supported: " + platformType);

		return ANDROID_APPLICATION;
	}

	private String createEndpointWithPlatformAndToken(String platformArn, String token) {
		CreatePlatformEndpointRequest cpeReq = new CreatePlatformEndpointRequest()
				.withPlatformApplicationArn(platformArn).withToken(token);
		CreatePlatformEndpointResult cpeRes = client.createPlatformEndpoint(cpeReq);

		return cpeRes.getEndpointArn();
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
}
