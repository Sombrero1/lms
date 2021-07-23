package com.example.demo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String author;


    @Column
    private String title;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @ManyToMany
    private Set<User> users;


    public Course(Long id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }

}