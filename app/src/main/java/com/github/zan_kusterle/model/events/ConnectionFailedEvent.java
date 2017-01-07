package com.github.zan_kusterle.model.events;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class ConnectionFailedEvent {

    private final String errorMessage;

    public ConnectionFailedEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
