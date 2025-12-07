package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.ActorDto;
import dev.studentpp1.streamingservice.movies.entity.Actor;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {

    public ActorDto toDto(Actor actor) {
        if(actor == null) return null;

        ActorDto dto = new ActorDto();
        dto.setId(actor.getId());
        dto.setName(actor.getName());
        dto.setSurname(actor.getSurname());
        dto.setBiography(actor.getBiography());

        return dto;
    }

    public Actor toEntity(ActorDto dto) {
        if(dto == null) return null;

        Actor actor = new Actor();
        actor.setName(dto.getName());
        actor.setSurname(dto.getSurname());
        actor.setBiography(dto.getBiography());

        return actor;
    }
}
