package com.everis.aws.notifications.data.structures;

public class AddTokenResponse {

	String subscriptionArn;
	
	public String getSubscriptionArn() {
		return subscriptionArn;
	}
	
	public void setSubcriptionArn(String subscriptionArn) {
		this.subscriptionArn = subscriptionArn;
	}
	
	public AddTokenResponse(String subscriptionArn) {
		this.subscriptionArn = subscriptionArn;
	}
	
	public AddTokenResponse() {
	}
}
