package com.zurich;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreamsClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@SpringBootApplication
@EnableEurekaClient
//@EntityScan(basePackages="com.zurich.entities")
public class RegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
	}

	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;

	@Bean
	public AWSCredentials awsCredentials() {
		return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
	}

	@Bean
	public AmazonDynamoDB amazonDynamoDB(AWSCredentials awsCredentials) {
		AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(awsCredentials);
		if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		}
		return amazonDynamoDB;
	}

	@Bean
	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDBClient amazonDynamoDBClient) {
		return new DynamoDBMapper(amazonDynamoDBClient);
	}

	@Bean
	public AmazonDynamoDBStreamsClient amazonDynamoDBStreamsAsyncClient(AWSCredentials awsCredentials) {
		return new AmazonDynamoDBStreamsClient(awsCredentials);
	}

	@Bean
	public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
		return new DynamoDB(amazonDynamoDB);
	}
}
