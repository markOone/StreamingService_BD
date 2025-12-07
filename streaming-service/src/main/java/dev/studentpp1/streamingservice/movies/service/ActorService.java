package dev.studentpp1.streamingservice.movies.service;

import dev.studentpp1.streamingservice.movies.dto.ActorDto;
import dev.studentpp1.streamingservice.movies.entity.Actor;
import dev.studentpp1.streamingservice.movies.mapper.ActorMapper;
import dev.studentpp1.streamingservice.movies.repository.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;


    public ActorService(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    public List<ActorDto> getAllActors() {
        return actorRepository.findAll()
                .stream()
                .map(actorMapper::toDto)
                .collect(Collectors.toList());
    }

    public ActorDto getActorById(Long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actor not found with id: " + id));
        return actorMapper.toDto(actor);
    }

    public ActorDto createActor(ActorDto actorDto) {
        Actor actor = actorMapper.toEntity(actorDto);

        Actor savedActor = actorRepository.save(actor);

        return actorMapper.toDto(savedActor);
    }

    public ActorDto updateActor(Long id, ActorDto actorDto) {
        Actor existingActor = actorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actor not found with id: " + id));

        existingActor.setName(actorDto.getName());
        existingActor.setSurname(actorDto.getSurname());
        existingActor.setBiography(actorDto.getBiography());

        Actor updatedActor = actorRepository.save(existingActor);

        return actorMapper.toDto(updatedActor);
    }

    public void deleteActor(Long id) {
        if (!actorRepository.existsById(id)) {
            throw new RuntimeException("Actor not found with id: " + id);
        }
        actorRepository.deleteById(id);
    }
}
