package com.everis.aws.notifications.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.everis.aws.notifications.entities.RegisterEntity;

@Repository
public class RegisterDAOImpl implements RegisterDAO {
	@Autowired
	private AmazonDynamoDB dynamoDB;

	@Override
	public List<RegisterEntity> findByIdentifier(String identifier) {
		List<RegisterEntity> registerList = new LinkedList<>();

		Map<String, AttributeValue> values = new HashMap<>();
		values.put(":val", new AttributeValue().withS(identifier));
		ScanRequest scanRequest = new ScanRequest().withLimit(100).withTableName("REGISTER_DEVICES")
				.withExpressionAttributeValues(values).withFilterExpression("identifier = :val")
				.withProjectionExpression("token");

		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for (Map<String, AttributeValue> item : scanResult.getItems()) {
			registerList.add(createRegister(item));
		}

		return registerList;
	}

	private RegisterEntity createRegister(Map<String, AttributeValue> dbRegister) {
		return new RegisterEntity(dbRegister.get(RegisterEntity.FIELD_TOKEN).getS(),
				dbRegister.get(RegisterEntity.FIELD_PLATFORM).getS(),
				dbRegister.get(RegisterEntity.FIELD_IDENTIFIER).getS());
	}
}