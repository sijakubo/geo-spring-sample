package com.newcubator.geosample.field.entrypoints;

import com.newcubator.geosample.field.domain.Field;
import java.util.Set;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;

import java.util.UUID;

public record FieldResource(UUID id, String name, Polygon<G2D> geometry, Set<String> properties) {

    public FieldResource(Field field) {
        this(field.getId(), field.getName(), field.getGeometry(), field.getProperties());
    }
}
