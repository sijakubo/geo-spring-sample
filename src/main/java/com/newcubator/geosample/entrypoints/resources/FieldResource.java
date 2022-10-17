package com.newcubator.geosample.entrypoints.resources;

import com.newcubator.geosample.domain.field.Field;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;

import java.util.UUID;

public record FieldResource(UUID id, String name, Polygon<G2D> geometry) {

    public FieldResource(Field field) {
        this(field.getId(), field.getName(), field.getGeometry());
    }
}
