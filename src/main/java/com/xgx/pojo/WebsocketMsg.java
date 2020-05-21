package com.xgx.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WebsocketMsg implements Serializable {

    private static final long serialVersionUID = 106592690777107318L;

    private List<String> userIds;

    private MessageInfo messageInfo;

}
