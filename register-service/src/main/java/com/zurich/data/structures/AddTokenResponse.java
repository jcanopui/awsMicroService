package com.zurich.data.structures;

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
