package com.example.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course author has to be filled")
    @Size(max=32, message = "Max length 32 chars")
    @Column
    private String author;


    @Column
    @NotBlank(message = "Course title has to be filled")
    @Size(max=32, message = "Max length 32 chars")
    @TitleCase()
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