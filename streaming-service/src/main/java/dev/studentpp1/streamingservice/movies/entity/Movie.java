package dev.studentpp1.streamingservice.movies.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "rating", precision = 3, scale = 1)
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @OneToMany(mappedBy = "movie")
    private List<Performance> performances;
}