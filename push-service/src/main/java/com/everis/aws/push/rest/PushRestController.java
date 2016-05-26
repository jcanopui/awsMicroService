package com.everis.aws.push.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.everis.aws.push.entities.RegisterEntity;
import com.everis.aws.push.entities.ResponseClass;
import com.everis.aws.push.sns.PushClient;
import com.everis.aws.push.sns.SendNotification;

@RestController
@RequestMapping("/")
public class PushRestController {

    @Autowired
    private PushClient pushClient;
    
    @Autowired
    private SendNotification sendNotification;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getInfo() {
		return "Push-service ready!";
    }

    @RequestMapping(value = "/topic/{message}", method = RequestMethod.GET)
    public ResponseClass sendTopic(@PathVariable String message) {
		ResponseClass response = sendNotification.broadcastNotification(message);
		
        return response;
    }
    
    @RequestMapping(value = "/{userId}/push/{message}", method = RequestMethod.GET)
    public ResponseClass sendPush(@PathVariable String userId, @PathVariable String message) {
		List<RegisterEntity> resgistryEntityList = pushClient.getTokens(userId);
        
		ResponseClass response = sendNotification.pushNotificationToUserDevices(message, resgistryEntityList);
		
        return response;
    }
}
