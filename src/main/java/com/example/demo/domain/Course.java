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
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(author, course.author) && Objects.equals(title, course.title) && Objects.equals(lessons, course.lessons) && Objects.equals(users, course.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, lessons, users);
    }
}