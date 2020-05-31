package com.lin.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_blacklist")
@Data
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creatTime;

    @ManyToOne
    @JoinColumn(name = "blacklist_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "blacklisted_id")
    private User usered;



    public BlackList() {
    }
}
