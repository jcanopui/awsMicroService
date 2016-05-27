package com.everis.aws.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreamsClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

@SpringBootApplication
@EnableEurekaClient
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

	@Value("${everis.aws.notifications.useProxiedDevEnvironment}")
	private Boolean useProxiedDevEnvironment;

	@Value("${everis.aws.notifications.proxyHost}")
	private String proxyHost;

	@Value("${everis.aws.notifications.proxyPort}")
	private Integer proxyPort;

	@Value("${everis.aws.notifications.username}")
	private String username;

	@Value("${everis.aws.notifications.password}")
	private String password;
	
	@Bean
	public AWSCredentials awsCredentials() {
		return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
	}

	@Bean
	public AmazonDynamoDB amazonDynamoDB(AWSCredentials awsCredentials, ClientConfiguration clientConfiguration) {
		AmazonDynamoDB amazonDynamoDB;
		if (useProxiedDevEnvironment)
			amazonDynamoDB = new AmazonDynamoDBClient(awsCredentials, clientConfiguration);
		else
			amazonDynamoDB = new AmazonDynamoDBClient(awsCredentials);
		if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		}
		amazonDynamoDB.setRegion(Region.getRegion(Regions.US_EAST_1));
		
		return amazonDynamoDB;
	}

	@Bean
	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDBClient amazonDynamoDBClient) {
		return new DynamoDBMapper(amazonDynamoDBClient);
	}

	@Bean
	public AmazonDynamoDBStreamsClient amazonDynamoDBStreamsAsyncClient(AWSCredentials awsCredentials) {
		AmazonDynamoDBStreamsClient amazonDynamoDBStreamsClient;
		amazonDynamoDBStreamsClient = new AmazonDynamoDBStreamsClient(awsCredentials);
		amazonDynamoDBStreamsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

		return amazonDynamoDBStreamsClient;
	}

	@Bean
	public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
		return new DynamoDB(amazonDynamoDB);
	}
	
	@Bean
	public ClientConfiguration proxiedClientConfiguration() {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setProtocol(Protocol.HTTP);

		if (useProxiedDevEnvironment != null && useProxiedDevEnvironment) {
			clientConfiguration.setProxyHost(proxyHost);
			clientConfiguration.setProxyPort(proxyPort);
			clientConfiguration.setProxyUsername(username);
			clientConfiguration.setProxyPassword(password);
		}
		clientConfiguration.setSocketTimeout(500);

		return clientConfiguration;
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public AmazonSNS amazonSNS(AWSCredentials awsCredentials, ClientConfiguration clientConfiguration) {
		AmazonSNSClient amazonSNSClient;
		if (useProxiedDevEnvironment)
			amazonSNSClient = new AmazonSNSClient(awsCredentials, clientConfiguration);
		else
			amazonSNSClient = new AmazonSNSClient(awsCredentials);
		amazonSNSClient.setRegion(Region.getRegion(Regions.US_EAST_1));
	
		return amazonSNSClient;
	}
}
