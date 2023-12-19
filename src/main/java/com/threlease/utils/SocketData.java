package com.threlease.utils;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Socket {
    private String action;
    private Object data;
}
