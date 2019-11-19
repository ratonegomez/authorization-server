package com.learn.oauth2.authorizationserver.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Notification {

    @Getter
    private final Integer id;

    @Getter
    private final String content;

    @Override
    public String toString() {
        return String.format("id=%s, content=%s ",id,content);
    }
}
