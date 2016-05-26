package com.everis.aws.push.sns;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.everis.aws.push.entities.NotificationEntity;
import com.everis.aws.push.entities.NotificationStatusEntity;
import com.everis.aws.push.entities.ResponseClass;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SendNotification {

	private static AmazonSNS client = null;

	private static ClientConfiguration clientConfig = null;

	private static AmazonDynamoDBClient dynamoClient = null;

	private static final String TOPIC_ARN_FOR_PUSHES = "arn:aws:sns:us-east-1:721597765533:PUSH_TO_DEVICE";

	private static final ObjectMapper objectMapper = new ObjectMapper();


	public SendNotification() {
		super();
		
		this.init();
	}

	private void init() {
		if (dynamoClient == null) {
			dynamoClient = new AmazonDynamoDBClient(new ClasspathPropertiesFileCredentialsProvider(), 
													getClientConfiguration());
			dynamoClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		}
		if (client == null) {
			client = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider(), 
										getClientConfiguration());
			client.setRegion(Region.getRegion(Regions.US_EAST_1));
		}
	}

	private static ClientConfiguration getClientConfiguration() {
		if (clientConfig == null) {
			clientConfig = new ClientConfiguration();
			clientConfig.setProtocol(Protocol.HTTP);

			clientConfig.setProxyHost("10.110.8.42");
			clientConfig.setProxyPort(8080);
			clientConfig.setProxyUsername("XXXXX");
			clientConfig.setProxyPassword("XXXXX");
		}
		return clientConfig;
	}

	public ResponseClass sendNotification(String tokenId, String message) {
		ResponseClass result = null;
		
		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setMessage(message);
		notificationEntity.setNotificationId(notificationEntity.getNotificationId()+1);
		notificationEntity.setTopic(true);
	
		result = sendTopic(notificationEntity);
		
		saveNotificationStatus(notificationEntity.getNotificationId());
		
		return result;
	}

	public ResponseClass sendTopic(NotificationEntity notificationEntity) {

		PublishRequest publishRequest = new PublishRequest();

		publishRequest.setTopicArn(TOPIC_ARN_FOR_PUSHES);

		PublishResult publishResult = publish(publishRequest, notificationEntity);

		return new ResponseClass(publishResult.getMessageId());
	}

	/**
	 * Update status to send to notification with specified notificationId
	 * 
	 * @param notificationId
	 *            notification identifier
	 */
	private void saveNotificationStatus(long notificationId) {

		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

		NotificationStatusEntity notificationStatus = new NotificationStatusEntity(notificationId,
				NotificationStatusEntity.SEND);
		mapper.save(notificationStatus);
	}

	private PublishResult publish(PublishRequest publishRequest, NotificationEntity notificationEntity) {

		publishRequest.setMessageStructure("json");

		String androidMessage = getAndroidMessage(notificationEntity);
		String appleMessage = getAppleMessage(notificationEntity);

		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("default", notificationEntity.getMessage());
		messageMap.put("APNS", appleMessage);
		messageMap.put("APNS_SANDBOX", appleMessage);
		messageMap.put("GCM", androidMessage);

		String message = jsonify(messageMap);

		System.out.println("{Message Body: " + message + "}");

		publishRequest.setMessage(message);

		PublishResult result = client.publish(publishRequest);

		return result;
	}

	private String getAppleMessage(NotificationEntity notificationEntity) {
		Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
		appMessageMap.put("alert", notificationEntity.getMessage());
		appMessageMap.put("notificationId", notificationEntity.getNotificationId());
		appleMessageMap.put("aps", appMessageMap);
		return jsonify(appleMessageMap);
	}

	private String getAndroidMessage(NotificationEntity notificationEntity) {
		Map<String, Object> androidMessageMap = new HashMap<String, Object>();
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
		appMessageMap.put("message", notificationEntity.getMessage());
		appMessageMap.put("notificationId", notificationEntity.getNotificationId());
		androidMessageMap.put("data", appMessageMap);
		return jsonify(androidMessageMap);
	}

	public static String jsonify(Object message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}
}
