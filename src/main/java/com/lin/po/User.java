package com.lin.po;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "t_user")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String headPortrait;
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creatTime;
    private byte locked = 1;

    @OneToOne
    private Teacher teacher;

    @ManyToMany(cascade = {CascadeType.PERSIST},fetch=FetchType.EAGER)
    @JsonIgnoreProperties(value = "users")
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private List<Blog> blogList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private List<Concern> concernList = new ArrayList<>();

    @OneToMany(mappedBy = "usered")
    @JsonIgnoreProperties(value = "user")
    private List<Concern> concernedList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private List<BlackList> blackListList = new ArrayList<>();

    @OneToMany(mappedBy = "usered")
    @JsonIgnoreProperties(value = "user")
    private List<BlackList> blackListedList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private List<Inform> informList = new ArrayList<>();

    @OneToMany(mappedBy = "usered")
    @JsonIgnoreProperties(value = "user")
    private List<Inform> informedList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private List<Collect> collectList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private List<Report> reportList = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role:roles){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRole()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked > 0? true:false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", description='" + description + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", email='" + email + '\'' +
                ", creatTime=" + creatTime +
                ", roles=" + roles +
                ", locked=" + locked +
                '}';
    }
}
