package com.xgx.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = -7548466714719311889L;

    private String type;

    private String message;

}
