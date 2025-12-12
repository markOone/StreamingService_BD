package dev.studentpp1.streamingservice.movies.controller;

import dev.studentpp1.streamingservice.movies.dto.DirectorDetailDto;
import dev.studentpp1.streamingservice.movies.dto.DirectorDto;
import dev.studentpp1.streamingservice.movies.dto.DirectorRequest;
import dev.studentpp1.streamingservice.movies.service.DirectorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<DirectorDetailDto> getDirectorDetails(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.getDirectorDetails(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorDto> getDirectorById(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.getDirectorById(id));
    }

    @PostMapping
    public ResponseEntity<DirectorDto> createDirector(@RequestBody @Valid DirectorRequest request) {
        return new ResponseEntity<>(directorService.createDirector(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DirectorDto> updateDirector(@PathVariable Long id, @RequestBody @Valid DirectorRequest request) {
        return ResponseEntity.ok(directorService.updateDirector(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirector(@PathVariable Long id) {
        directorService.deleteDirector(id);
        return ResponseEntity.noContent().build();
    }
}