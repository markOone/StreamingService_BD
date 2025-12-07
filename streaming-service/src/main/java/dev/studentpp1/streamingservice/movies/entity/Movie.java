package dev.studentpp1.streamingservice.movies.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    private String title;
    private String description;
    private Integer year;
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private List<Performance> performances;
    

}
