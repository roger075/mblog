package com.lin.po;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_comment")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentTime;

    @ManyToOne
    private Blog blog;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyCommentList = new ArrayList<>();

    @ManyToOne
    private Comment parentComment;

    public Comment() {
    }
}
