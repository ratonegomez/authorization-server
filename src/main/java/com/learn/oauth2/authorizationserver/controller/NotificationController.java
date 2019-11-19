package com.learn.oauth2.authorizationserver.controller;

import com.learn.oauth2.authorizationserver.model.Notification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequestMapping(value = "/notification")
@RestController
public class NotificationController {

    private final List<Notification> notifications=new ArrayList<>();

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public void add( Notification notification){
        notifications.add(notification);
    }

    @RequestMapping(value="/get",method = RequestMethod.GET)
    public Notification get(@RequestParam(value = "id") String id){
        return Stream.ofNullable(notifications).flatMap(i->i.stream()).filter(i->i.getId()==Integer.parseInt(id)).findFirst().orElse(new Notification(-1,""));
    }
}
