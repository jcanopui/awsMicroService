package com.everis.aws.push.sns;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.everis.aws.push.entities.RegisterEntity;

@FeignClient("register-service")
public interface PushClient {

    @RequestMapping(method = RequestMethod.GET, value = "/tokens/{identifier}")
    List<RegisterEntity> getTokens(@PathVariable("identifier") String userId);
}