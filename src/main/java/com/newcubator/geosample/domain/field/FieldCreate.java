package com.newcubator.geosample.domain.field;

import lombok.RequiredArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.UUID;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class FieldCreate {

    private final FieldRepository fieldRepository;

    public Field handle(@NotNull UUID id, @NotNull String name, @NotNull Polygon<G2D> geometry) {
        return fieldRepository.save(new Field(id, name, geometry));
    }
}
