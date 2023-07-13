package com.newcubator.geosample.field.domain;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FieldCreate {

    private final FieldRepository fieldRepository;

    public Field handle(@NotNull UUID id, @NotNull String name, @NotNull Polygon<G2D> geometry, @NotNull Set<String> properties) {
        if (!fieldRepository.isValid(name, geometry)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid create field request");
        }
        return fieldRepository.save(new Field(id, name, geometry, properties, null));
    }
}
