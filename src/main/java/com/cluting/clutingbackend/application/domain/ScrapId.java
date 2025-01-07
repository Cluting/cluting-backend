package com.cluting.clutingbackend.application.domain;

import java.io.Serializable;

public class ScrapId implements Serializable {
    private Long user;
    private Long recruit;

    public ScrapId(){}

    public ScrapId(Long user,Long recruit){
        super();
        this.user = user;
        this.recruit = recruit;
    }
}
