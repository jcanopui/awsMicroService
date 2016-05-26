package com.everis.aws.push.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.everis.aws.push.entities.NotificationEntity;
import com.everis.aws.push.entities.RegisterEntity;
import com.everis.aws.push.entities.ResponseClass;
import com.everis.aws.push.sns.PushClient;
import com.everis.aws.push.sns.SendNotification;

@RestController
@RequestMapping("/{userId}/push")
public class PushRestController {

	private int incNotificationId = 0;
    @Autowired
    private PushClient pushClient;
    
    @Autowired
    private SendNotification sendNotification;

    @RequestMapping(value = "/{message}", method = RequestMethod.GET)
    public ResponseClass sendPush(@PathVariable String userId, @PathVariable String message) {

		List<RegisterEntity> resgistryEntityList = pushClient.getTokens(userId);
        
		resgistryEntityList.stream().forEach(System.out::println);

		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setMessage(message);
		notificationEntity.setNotificationId(++incNotificationId);
		notificationEntity.setTopic(true);
		
		resgistryEntityList.stream().
		filter(re1 -> "android".equals(re1.getPlatform())).
			forEach((re2) -> { System.out.println("Content: " + re2.toString()); });
	
		ResponseClass response = sendNotification.sendTopic(notificationEntity);
		
        return response;
    }
}
