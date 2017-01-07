package com.github.zan_kusterle.model;

import java.io.Serializable;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class Intention implements Serializable {

    private final IntentionType type;

    public Intention(IntentionType type) {
        this.type = type;
    }
}
