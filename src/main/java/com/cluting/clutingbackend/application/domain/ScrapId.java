package com.cluting.clutingbackend.application.domain;

import java.io.Serializable;

public class ScrapId implements Serializable {
    private Long user;
    private Long board;

    public ScrapId(){}

    public ScrapId(Long user,Long board){
        super();
        this.user = user;
        this.board = board;
    }
}
