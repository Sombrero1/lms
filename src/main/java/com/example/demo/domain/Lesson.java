package com.example.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lessons")
@NoArgsConstructor
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Lob
    @Column
    private String text;

    @ManyToOne(optional = false)
    private Course course;

    public Lesson(String title, String text, Course course) {
        this.title = title;
        this.text = text;
        this.course = course;
    }


    public Lesson(String h, String fdasfsdfd) {
        title = h;
        text = fdasfsdfd;
    }

    public Lesson(long l) {
        this.id = l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(title, lesson.title) && Objects.equals(text, lesson.text) && Objects.equals(course, lesson.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, course);
    }
}
