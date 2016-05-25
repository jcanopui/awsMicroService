package com.zurich.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zurich.Register;
import com.zurich.data.structures.AddTokenResponse;
import com.zurich.db.RegisterDAO;
import com.zurich.entities.RegisterEntity;

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

	@RequestMapping(method = RequestMethod.GET)
	RegisterEntity putToken(@PathVariable String identifier) {
		return registerDAO.findByIdentifier(identifier);
	}
}