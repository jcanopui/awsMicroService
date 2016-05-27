package com.everis.aws.push.sns;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.everis.aws.push.entities.NotificationEntity;
import com.everis.aws.push.entities.NotificationStatusEntity;
import com.everis.aws.push.entities.RegisterEntity;
import com.everis.aws.push.entities.ResponseClass;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SendNotification {

	@Autowired
	private AmazonSNS client;

	@Autowired
	private AmazonDynamoDB dynamoClient;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${everis.aws.notifications.topicForPushes}")
	private String topicForPushes;

	public ResponseClass broadcastNotification(String message) {
		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setMessage(message);
		notificationEntity.setNotificationId(new Random().nextLong());
		notificationEntity.setTopic(true);

		ResponseClass result = sendTopic(notificationEntity);

		saveNotification(notificationEntity);

		return result;
	}

	public ResponseClass pushNotificationToUserDevices(String message, List<RegisterEntity> resgistryEntityList) {
		List<ResponseClass> resultClassList = new LinkedList<>();

		resgistryEntityList.stream().forEach(p -> {
			NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setMessage(message);
			notificationEntity.setNotificationId(new Random().nextLong());
			notificationEntity.setTopic(false);
			notificationEntity.setTargetAWS(p.getEndpointARN());

			resultClassList.add(sendTopic(notificationEntity));

			saveNotification(notificationEntity);
		});

		StringBuilder resultBuilder = new StringBuilder();
		resultClassList.stream().forEach(p -> {
			resultBuilder.append(p.getMessageId());
			resultBuilder.append(",");
		});

		// Remove last comma
		String result = resultBuilder.toString();
		if (result.length() > 1)
			result = result.substring(0, result.length() - 1);

		return new ResponseClass("[" + result + "]");
	}

	public ResponseClass sendTopic(NotificationEntity notificationEntity) {

		PublishRequest publishRequest = new PublishRequest();

		if (notificationEntity.isTopic())
			publishRequest.setTopicArn(topicForPushes);
		else
			publishRequest.setTargetArn(notificationEntity.getTargetAWS());

		PublishResult publishResult = publish(publishRequest, notificationEntity);

		return new ResponseClass(publishResult.getMessageId());
	}

	private void saveNotification(NotificationEntity notificationEntity) {
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

		mapper.save(notificationEntity);

		NotificationStatusEntity notificationStatus = new NotificationStatusEntity(
				notificationEntity.getNotificationId(), NotificationStatusEntity.SEND);
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

	public String jsonify(Object message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}
}
