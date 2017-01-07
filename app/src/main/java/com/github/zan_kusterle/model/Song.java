package com.github.zan_kusterle.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by bencz on 2016. 12. 08..
 */

@Getter
@Builder
public class Song implements Serializable {

    private final String id;
    private final String originBluetoothDeviceAddress;
    private final String url;
    private final String videoId;
    private final String title;
}
