package com.everis.aws.notifications.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everis.aws.notifications.business.Register;
import com.everis.aws.notifications.data.structures.AddTokenResponse;
import com.everis.aws.notifications.db.RegisterDAO;
import com.everis.aws.notifications.entities.RegisterEntity;

@RestController
@RequestMapping("/tokens")
class SNSTokenRestController {

	@Autowired
	private RegisterDAO registerDAO; 
	
	@Autowired
	private Register register;

	@RequestMapping(method = RequestMethod.POST)
	AddTokenResponse putToken(@RequestParam String token, @RequestParam String platform, @RequestParam String identifier) {
		return register.registerToken(token, platform, identifier);
	}

	@RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
	List<RegisterEntity> putToken(@PathVariable String identifier) {
		return registerDAO.findByIdentifier(identifier);
	}
}