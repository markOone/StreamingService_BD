package dev.studentpp1.streamingservice.movies.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
