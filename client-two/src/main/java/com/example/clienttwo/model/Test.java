package com.example.clienttwo.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "test")
public class Test implements Serializable {
    @Id
    private Integer id;

    private String name;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}