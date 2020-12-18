package com.xgx.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = -7712107222673309080L;

    private Integer id;
    private String name;
}
