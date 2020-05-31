package com.lin.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_blog")
@Data
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String digest;
    private String original;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    private Long views;
    private Long likes;
    private String open;
    private String comment;
    private String manuscript;


    public void increaseViews(){
        this.views++;
    }
    public void increaseLikes(){
        this.likes++;
    }
    public void decreaseLikes(){
        this.likes--;
    }

    @ManyToOne
    @JsonIgnoreProperties(value = "blogList")
    private Module module;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JsonIgnoreProperties(value = "blogList")
    private List<Tag> tagList = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "blogList")
    private User user;

    @OneToMany(mappedBy = "blog")
    @JsonIgnoreProperties(value = "blog")
    private List<Collect> collectedList = new ArrayList<>();

    @OneToMany(mappedBy = "bloged")
    @JsonIgnoreProperties(value = "bloged")
    private List<Report> reportedList = new ArrayList<>();

    @OneToMany(mappedBy = "blog")
    @JsonIgnoreProperties(value = "blog")
    private List<Comment> commentList = new ArrayList<>();

    public Blog(String title, String digest, String original, String content, Date updateTime, Long views, Long likes, String open, String comment, Module module) {
        this.title = title;
        this.digest = digest;
        this.original = original;
        this.content = content;
        this.updateTime = updateTime;
        this.views = views;
        this.likes = likes;
        this.open = open;
        this.comment = comment;
        this.module = module;
    }

    public Blog() {
    }
}
