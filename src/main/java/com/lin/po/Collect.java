package com.lin.po;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_collect")
@Data
public class Collect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creatTime;

    @ManyToOne
    @JoinColumn(name = "collect_Uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "collected_Bid")
    private Blog blog;

    public Collect() {
    }
}
