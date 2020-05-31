package com.lin.po;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_report")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportTime;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reported_id")
    private Blog bloged;

    public Report() {
    }
}
