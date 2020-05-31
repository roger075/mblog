package com.lin.po;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "t_teacher")
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String teacherName;
    private Long idNumber;


    @OneToOne(mappedBy = "teacher")
    private User user;

    public Teacher() {
    }
}
