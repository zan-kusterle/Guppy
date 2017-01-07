package com.github.zan_kusterle.model.events;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class FileOperationErrorEvent {

    private final String errorMessage;

    public FileOperationErrorEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
