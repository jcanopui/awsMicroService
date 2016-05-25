package com.zurich.db;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.zurich.entities.RegisterEntity;

@Repository
public class RegisterDAOImpl implements RegisterDAO {
    @Autowired
    private AmazonDynamoDB dynamoDB;

    @Override
    public RegisterEntity findByIdentifier(String identifier) {
        Map<String, AttributeValue> lastKeyEvaluated;
        do
        {
            Map<String, AttributeValue> values = new HashMap<>();
            values.put(":val", new AttributeValue().withS(identifier));
            ScanRequest scanRequest = new ScanRequest()
                    .withLimit(100)
                    .withTableName("REGISTER_DEVICES")
                    .withExpressionAttributeValues(values)
                    .withFilterExpression("identifier = :val")
                    .withProjectionExpression("token");

            ScanResult scanResult = dynamoDB.scan(scanRequest);

            //scanResult.getItems().stream().forEach(System.out::println);

            lastKeyEvaluated = scanResult.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return null;    }
}
