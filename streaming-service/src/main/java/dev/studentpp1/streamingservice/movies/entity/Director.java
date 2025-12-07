package dev.studentpp1.streamingservice.movies.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "director")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "surname",  nullable = false, length = 100)
    private String surname;

    @Column(name = "biography", columnDefinition = "TEXTS")
    private String biography;

    @OneToMany(mappedBy = "director")
    private List<Movie> movies;
}