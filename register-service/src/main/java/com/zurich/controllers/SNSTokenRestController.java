package com.zurich.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zurich.Register;
import com.zurich.entities.ResponseClass;

@RestController
@RequestMapping("/token")
class SNSTokenRestController {
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseClass getToken(@RequestParam String token, @RequestParam String platform, @RequestParam String identifier) {
		return Register.registerToken(token, platform, identifier);
	}
}