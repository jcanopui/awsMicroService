package com.everis.aws.notifications;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;

public class SNSTest {
	public static void main(String[] args) {
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);

		clientConfig.setProxyHost("10.110.8.42");
	    clientConfig.setProxyPort(8080);
	    clientConfig.setProxyUsername("rmartinez");
	    clientConfig.setProxyPassword("Rm30032016");

	    // create a new SNS client and set endpoint
		AmazonSNSClient snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider(), clientConfig);
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

		// create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest("RMTopic");
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		// print TopicArn
		System.out.println(createTopicResult);
		// get request id for CreateTopicRequest from SNS metadata
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
	}
}
