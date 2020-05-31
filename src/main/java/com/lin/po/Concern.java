package com.lin.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_concern")
@Data
public class Concern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creatTime;

    @ManyToOne
    @JoinColumn(name = "concern_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "concerned_id")
    private User usered;

    public Concern() {
    }
}
