package com.lin.po;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_tag")
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private byte usable;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ManyToMany(mappedBy = "tagList")
    private List<Blog> blogList = new ArrayList<>();

    public Tag() {
    }
}
