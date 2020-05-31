package com.lin.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_inform")
@Data
public class Inform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    private byte readed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime;

    @ManyToOne
    @JoinColumn(name = "inform_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "informed_id")
    private User usered;



    public Inform() {
    }
}
