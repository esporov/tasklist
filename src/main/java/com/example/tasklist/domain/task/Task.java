package com.example.tasklist.domain.task;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    private String title;
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Status status;


    private LocalDateTime expirationDate;
}
