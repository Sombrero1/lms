package com.example.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
