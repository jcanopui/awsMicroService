package com.everis.aws.notifications.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.everis.aws.notifications.business.Register;
import com.everis.aws.notifications.data.structures.AddTokenRequest;
import com.everis.aws.notifications.data.structures.AddTokenResponse;
import com.everis.aws.notifications.db.RegisterDAO;
import com.everis.aws.notifications.entities.RegisterEntity;

@RestController
@RequestMapping("/")
class SNSTokenRestController {

	@Autowired
	private RegisterDAO registerDAO; 
	
	@Autowired
	private Register register;

	@RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getInfo() {
		return "Register-service ready!";
    }

	@RequestMapping(value="/tokens", method = RequestMethod.POST)
	public AddTokenResponse registerToken(AddTokenRequest request) {
		return register.registerToken(request.getToken(), request.getPlatform(), request.getIdentifier(), request.getProtocol());
	}

	@RequestMapping(value = "/tokens/{identifier}", method = RequestMethod.GET)
	public List<RegisterEntity> getToken(@PathVariable String identifier) {
		return registerDAO.findByIdentifier(identifier);
	}
}